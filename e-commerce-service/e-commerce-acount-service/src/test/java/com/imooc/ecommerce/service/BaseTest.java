package com.imooc.ecommerce.service;

import com.imooc.ecommerce.filter.AccessContext;
import com.imooc.ecommerce.vo.LoginUserInfo;
import org.apache.kafka.common.security.auth.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试用例基类，填充登录用户信息
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTest {

    protected final LoginUserInfo loginUserInfo = new LoginUserInfo(
            10L,"shen@123.com"
    );

    @Before
    public void init(){
        AccessContext.setLoginUserInfo(loginUserInfo);
    }

    @After
    public void destory(){
        AccessContext.clearLoginUserInfo();
    }
}
