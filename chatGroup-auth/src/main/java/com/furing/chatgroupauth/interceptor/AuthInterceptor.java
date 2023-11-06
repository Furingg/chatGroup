package com.furing.chatgroupauth.interceptor;

import com.furing.chatgroupauth.constant.TokenConstant;
import com.furing.chatgroupauth.utils.JwtTools;
import com.furing.commons.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author furing
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class AuthInterceptor implements Filter {

    private static final String WHITE = "/login";

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String path = request.getServletPath();
        // 白名单路径放行
        if (path.equals(WHITE)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 如果token为空，过滤
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token) || "null".equals(token)) {
            response.sendError(TokenConstant.TOKEN_NULL_CODE, TokenConstant.TOKEN_NULL);
            return;
        }

        // 如果token解析失败，过滤
        Long userId = JwtTools.verify(token);
        if (userId == 0) {
            response.sendError(TokenConstant.TOKEN_PARSE_FAIL_CODE, TokenConstant.TOKEN_PARSE_FAIL);
            return;
        }

        String freshToken = JwtTools.refreshToken(token);
        // 如果token不在redis缓存中，过滤
        if (redisUtil.getKet(token) == null) {
            response.sendError(TokenConstant.TOKEN_INVALID_CODE, TokenConstant.TOKEN_INVALID_FAIL);
            // 判断是否过期，返回true为未过期
            if (!Objects.isNull(freshToken)) {
                redisUtil.del(token);
                redisUtil.set(freshToken, userId, TokenConstant.EXPIRE_TIME);
                response.addHeader("Authorization", freshToken);
                filterChain.doFilter(request, response);
                return;
            }
            return;
        }

        // 如果鉴权成功，则更新token
        String newToken = JwtTools.createToken(userId);
        // 删除老token
        if (!redisUtil.del(token)) {
            response.sendError(TokenConstant.TOKEN_DELETE_FAIL_CODE, TokenConstant.TOKEN_DELETE_FAIL);
            return;
        }
        // 加入新token
        if (!redisUtil.set(newToken, userId, TokenConstant.EXPIRE_TIME)) {
            response.sendError(TokenConstant.TOKEN_UPDATE_FAIL_CODE, TokenConstant.TOKEN_UPDATE_FAIL);
            return;
        }
        response.addHeader("Authorization", newToken);
        filterChain.doFilter(request, response);
    }
}
