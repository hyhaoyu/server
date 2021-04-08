package com.haoyu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenWorker {

    //设置过期时间
    private static final long EXPIRE_DATE = 30 * 60 * 100000;
    //token秘钥
    private static final String TOKEN_SECRET = "LiHaoYu202103";

    //算法
    private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

    //生成token
    public static String generateToken (String id,String password, Integer role){

        String token = "";
        //过期时间
        Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
        //设置头部信息
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        //携带username，password信息，生成签名
        token = JWT.create()
                .withHeader(header)
                .withClaim("id",id)
                .withClaim("password",password)
                .withClaim("role",role).withExpiresAt(date)
                .sign(algorithm);
        return token;

    }

    //解密token
    public static DecodedJWT verifyToken(String token){

        try {
           return verifier.verify(token);
        } catch (RuntimeException e) {
            throw new RuntimeException("登录状态异常,请重新登录");
        }

    }

    //获得token中的id;
    public static String getIdFromJWT(String token){
        if(StringUtils.isBlank(token)){
            throw new RuntimeException("请先登录");
        }
        DecodedJWT jwt = verifyToken(token);
        if(System.currentTimeMillis() > jwt.getExpiresAt().getTime()){
            throw new RuntimeException("登录过期，请重新登录");
        }
        return jwt.getClaim("id").asString();
    }

    //获取token中的用户角色
    public static Integer getRoleFromJWT(String token){
        if(StringUtils.isBlank(token)){
            throw new RuntimeException("请先登录");
        }
        DecodedJWT jwt = verifyToken(token);
        if(System.currentTimeMillis() > jwt.getExpiresAt().getTime()){
            throw new RuntimeException("登录过期，请重新登录");
        }
        return jwt.getClaim("role").asInt();
    }
}
