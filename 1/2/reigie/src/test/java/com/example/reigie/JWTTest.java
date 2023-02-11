package com.example.reigie;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.example.reigie.common.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Date;

@Slf4j
public class JWTTest {
    private static final String secret =  "jkasdflkadhslkfjl1523152132kwdufhawrkujfhipwragfhuiqawfhk";

    @Test
    public void test01(){
        TokenUser tokenUser = new TokenUser(123L,"test");
        //创建一个令牌构建者对象
        JWTCreator.Builder builder = JWT.create();
        //创建一个加密算法
        Algorithm algorithm = Algorithm.HMAC256(secret);//参数为密钥

        //给构建者设定令牌需要携带的信息
        builder.withClaim(TokenUser.CLAIM_NAME_ID, tokenUser.getId())
                .withClaim(TokenUser.CLAIM_NAME_USERNAME, tokenUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60));
        String token = builder.sign(algorithm);
        log.debug("Token:{}",token);
    }

    @Test
    public void test02(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJDTEFJTV9OQU1FX0lEIjoiMTIzIiwiQ0xBSU1fTkFNRV9VU0VSTkFNRSI6InRlc3QiLCJleHAiOjE2NTM3ODk0NzZ9.ucub6mgtzkThskFL4ucmQ8WSPDti9xAL7KsEqaRv3_k";

        //获取解码后的令牌
        DecodedJWT decodedJWT = JWT.decode(token);

        String id = decodedJWT.getClaim(TokenUser.CLAIM_NAME_ID).asString();
        String username = decodedJWT.getClaim(TokenUser.CLAIM_NAME_USERNAME).asString();

        log.debug("id:{},username:{}",id,username);

    }

    @Test
    public void test03(){
        //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJDTEFJTV9OQU1FX0lEIjoiMTIzIiwiQ0xBSU1fTkFNRV9VU0VSTkFNRSI6InRlc3QiLCJleHAiOjE2NTM3ODk0NzZ9.ucub6mgtzkThskFL4ucmQ8WSPDti9xAL7KsEqaRv3_k";
        //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJDTEFJTV9OQU1FX0lEIjoiMTIzIiwiQ0xBSU1fTkFNRV9VU0VSTkFNRSI6InRlc3QiLCJleHAiOjE2NTM3OTA4OTB9.6_yPha8uKbv5ipzESGc57MZW6dWKoON2jq9lXmrRmFQ";
        //String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJDTEFJTV9OQU1FX0lEIjoxLCJDTEFJTV9OQU1FX1VTRVJOQU1FIjoiYWRtaW4iLCJleHAiOjE2NTM3OTk3Nzl9.uEA37ApMlbMHLka2rKkQzo7MSYDQJLf_WkZGIFf-x_Q";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJDTEFJTV9OQU1FX0lEIjoxLCJDTEFJTV9OQU1FX1VTRVJOQU1FIjoiYWRtaW4iLCJleHAiOjE2NTM4ODY1MDd9.rTJEY-7mp8wjBwBalBdDddK7rhO_pdnpwAaRfwmv_gI";
        //通过生成令牌使用的加密算法和密钥来获取令牌校验器的构建者
        Verification verification = JWT.require(Algorithm.HMAC256(secret));

        //通过构建者来设置那些信息进行校验
                //过期时间默认进行校验不需要再额外写
        verification.withClaim(TokenUser.CLAIM_NAME_ID,1)
                .withClaim(TokenUser.CLAIM_NAME_USERNAME,"admin");
        //通过构建器创建一个验证器
        JWTVerifier verifier = verification.build();

        //校验令牌
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            log.debug("id:{},username:{}",decodedJWT.getClaim(TokenUser.CLAIM_NAME_ID),decodedJWT.getClaim(TokenUser.CLAIM_NAME_USERNAME));
        } catch(TokenExpiredException e){
            log.debug("令牌过期！");
        }catch (JWTVerificationException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test04(){
        System.out.println(System.currentTimeMillis());
        System.out.println((System.currentTimeMillis()%10000000000L)/1000000);
        System.out.println((System.currentTimeMillis()%10000000000L)/1000000+Integer.valueOf("1234567890".substring(0,4)));
        //1653895699893
        //1653895718152 1653895772577 1653895780942
    }


}
