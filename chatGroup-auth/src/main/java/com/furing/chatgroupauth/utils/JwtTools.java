package com.furing.chatgroupauth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author furing
 */
public class JwtTools {
    /**
     * 过期时间 30分钟
     */
    private static final long EXPIRE_TIME = 60 * 1000;

    /**
     * 允许过期时间 2小时
     */
    private static final long ALLOW_EXPIRE_TIME = 30 * 60 * 1000 * 4;

    /**
     * 密钥
     */
    private static final String TOKEN_SECRET = "furingPrivateKey";

    public static String createToken(Long userId) {
        // 私钥和加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

        Map<String, Object> header = new HashMap<>(2);
        header.put("Type", "Jwt");
        header.put("alg", "HS256");
        // 返回Token
        return JWT.create().
                withHeader(header).
                withClaim("userId", userId).
                withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME)).
                sign(algorithm);
    }

    public static Long verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        return verify.getClaim("userId").asLong();
    }

    public static String refreshToken(String token) {
        // 当前时间戳
        Instant now = Instant.now();
        DecodedJWT jwt = JWT.decode(token);
        // jwt 的过期时间戳
        Instant exp = jwt.getExpiresAt().toInstant();
        if ((now.getEpochSecond() - exp.getEpochSecond()) > ALLOW_EXPIRE_TIME / 1000) {
            return null;
        }
        Long userId = verify(token);

        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        Map<String, Object> header = new HashMap<>(2);
        header.put("Type", "Jwt");
        header.put("alg", "HS256");

        String freshToken = JWT.create().
                withHeader(header).
                withClaim("userId",userId).
                withExpiresAt(Date.from(exp.plusMillis(EXPIRE_TIME))).
                sign(algorithm);
        return freshToken;
    }
}
