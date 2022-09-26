package com.imooc.ecommerce.conf;


import com.imooc.ecommerce.filter.LoginUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Web Mvc 配置
 */
@Configuration
public class ImoocWebMvcConfig extends WebMvcConfigurationSupport {

    //添加拦截器配置
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {

        //添加用户身份统一登录拦截的拦截器
        registry.addInterceptor(new LoginUserInfoInterceptor())
                .addPathPatterns("/**").order(0);
    }
}
