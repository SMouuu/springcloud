package com.imooc.ecommerce.service;


import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.dao.EcommerceUserDao;
import com.imooc.ecommerce.entity.EcommerceUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * EcommerceUser 相关测试
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class EcommerceUserTest {

    @Autowired
    private EcommerceUserDao ecommerceUserDao;

    @Test
    public void createUserRecord(){

        EcommerceUser ecommerceUser=new EcommerceUser();
        ecommerceUser.setUsername("ImoocQinyi@imooc.com");
        ecommerceUser.setPassword(MD5.create().digestHex("123"));
        ecommerceUser.setExtraInfo("{}");
        log.info("save user: [{}]", JSON.toJSON(ecommerceUserDao.save(ecommerceUser)));
    }

}
