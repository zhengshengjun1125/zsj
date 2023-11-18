package com.zsj.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Component
public class JwtUtil {

    /**
     * @param acc account
     * @return token
     */
    public static String getJwtToken(String acc) {
        HashMap<String, Object> map = new HashMap<>();
        return JWT.create()
                .withHeader(map) //默认
                .withClaim("account", acc)
                .withClaim("id", String.valueOf(UUID.randomUUID()).replace("-",""))
                .sign(Algorithm.HMAC256(acc));
    }

    /**
     * @param username username
     * @param password password
     * @param expire   expire
     * @return token
     */
    public static String getJwtToken(String username, String password, Date expire) {
        HashMap<String, Object> map = new HashMap<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 20);//过期时间
        return JWT.create()
                .withHeader(map)
                .withClaim("userId", 20)//payload
                .withClaim("username", username)
                .withExpiresAt(expire) //指定令牌过期时间
                .sign(Algorithm.HMAC256("fdahuifeuw78921"));
    }

}
