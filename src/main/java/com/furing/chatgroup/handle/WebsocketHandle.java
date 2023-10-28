package com.furing.chatgroup.handle;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.furing.chatgroup.config.NettyConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author furing
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebsocketHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("用户下线：[{}]", ctx.channel().id().asLongText());
        NettyConfig.getChannelGroup().remove(ctx.channel());
        // 删除通道
        removeUserId(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的客户端连接：[{}]", ctx.channel().id().asLongText());
        NettyConfig.getChannelGroup().add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务器收到消息:[{}]", msg.text());

        // 获取用户id 关联channel 判断是不是json数据
        if (JSONUtil.isJson(msg.text())) {
            JSONObject jsonObject = JSONUtil.parseObj(msg.text());

            Long userId = jsonObject.getLong("userId");
            NettyConfig.getChannelMap().put(userId, ctx.channel());
            // 将用户id作为自定义属性加入到channel中，方便随时channel中获取用户id
            AttributeKey<Long> key = AttributeKey.valueOf("userId");
            ctx.channel().attr(key).setIfAbsent(userId);
        }
        // 返回给客户端数据
        ctx.channel().writeAndFlush(new TextWebSocketFrame("收到客户端发送的信息：" + msg.text()));
    }

    /**
     * 异常捕获
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("异常:{}", cause.getMessage());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();
    }

    /**
     * 删除用户与channel的对眼关系
     */
    private void removeUserId(ChannelHandlerContext ctx) {
        AttributeKey<Long> idKey = AttributeKey.valueOf("userId");
        Long userId = ctx.channel().attr(idKey).get();
        // 判断id空值
        if (Objects.isNull(userId)) {
            return;
        }
        log.info("用户[{}]下线了", userId);
        NettyConfig.getChannelMap().remove(userId);
    }
}
