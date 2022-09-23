package com.imooc.ecommerce.service;

import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.util.TokenParseUtil;
import com.imooc.ecommerce.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * JWT 相关服务测试类
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class JWTServiceTest {

    @Autowired
    private IJWTService ijwtService;

    @Test
    public void testGenerateAndParseToekn() throws Exception{

        String jwtToken = ijwtService.generateToken(
                "ImoocQinyi@imooc.com",
                "202cb962ac59075b964b07152d234b70"
        );
        log.info("jwt token is: [{}]",jwtToken);

        LoginUserInfo userInfo = TokenParseUtil.parseUserInfoFromToken(jwtToken);
        log.info("parse token: [{}]", JSON.toJSONString(userInfo));

    }
}
