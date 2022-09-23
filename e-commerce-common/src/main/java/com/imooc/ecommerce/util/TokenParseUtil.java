package com.imooc.ecommerce.util;


import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.constant.CommonConstant;
import com.imooc.ecommerce.vo.LoginUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

/**
 * JWT token 解析工具类
 */
public class TokenParseUtil {
    public static LoginUserInfo parseUserInfoFromToken(String token) throws Exception{

        if (null == token){
            return null;
        }

        Jws<Claims> claimsJws = parseToken(token,getPublicKey());
        Claims body = claimsJws.getBody();

        //如果token已经过期返回null
        if(body.getExpiration().before(Calendar.getInstance().getTime())){
            return null;
        }

        //返回token 中保存的信息
        return JSON.parseObject(
                body.get(CommonConstant.JWT_USER_INFO_KEY).toString(),
                LoginUserInfo.class
        );
    }

    /**
     * 通过公钥去解析JWT Token
     */
    private static Jws<Claims> parseToken(String token,PublicKey publicKey){
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }


    /**
     * 根据本地存储公钥获取到 PublicKey对象
     */
    private static PublicKey getPublicKey() throws Exception{

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(CommonConstant.PUBLIC_KEY));
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);

    }
}
