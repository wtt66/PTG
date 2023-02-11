package com.example.reigie.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
public class TokenUtils {

    public static String SECRET = "jkasdflkadhslkfjl1523152132kwdufhawrkujfhipwragfhuiqawfhk";

    public static long expire_time = 60*60*24; //默认登录后用户的Token的过期时间，默认设置为1天，单位：秒

    /**
     * 生成一个登录令牌
     * @param tokenUser
     * @return
     */
    public static String loginSign(TokenUser tokenUser){
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTCreator.Builder builder =  JWT.create().withClaim(TokenUser.CLAIM_NAME_ID,tokenUser.getId())
                                    .withClaim(TokenUser.CLAIM_NAME_USERNAME,tokenUser.getUsername());
        String token = builder.withExpiresAt(new Date(System.currentTimeMillis()+expire_time*1000)).sign(algorithm);
        return token;
    }

    /**
     * 从客户端令牌中获取用户信息
     * @param clientToken
     * @return
     */
    public static TokenUser getTokenUser(String clientToken){
        if(!StringUtils.hasText(clientToken)){//传来的令牌为空
            throw new NotAuthenticationException("令牌为空！请登录");
        }
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.decode(clientToken);
        } catch (JWTDecodeException e) {
            throw new NotAuthenticationException("令牌错误！");
        }


        Long id =  decodedJWT.getClaim(TokenUser.CLAIM_NAME_ID).asLong();
        String userName = decodedJWT.getClaim(TokenUser.CLAIM_NAME_USERNAME).asString();

        if(id==null||!StringUtils.hasText(userName)){
            throw new NotAuthenticationException("令牌中无用户信息！请登录");
        }

        return new TokenUser(id,userName);
    }

    /**
     * 校验客户端令牌，如果验证不通过抛出自定义的异常NotAuthenticationException
     * @param clientToken
     * @return
     */
    public static TokenUser verify(String clientToken){

        TokenUser tokenUser = getTokenUser(clientToken);

        JWTVerifier verifier =  JWT.require(Algorithm.HMAC256(SECRET)).build();

        try {
            verifier.verify(clientToken);
            return tokenUser;
        } catch(TokenExpiredException e) {
            throw new NotAuthenticationException("令牌失效！请登录");
        }catch (JWTVerificationException e) {
            throw new NotAuthenticationException("令牌非法！请登录");
        }

    }


}
