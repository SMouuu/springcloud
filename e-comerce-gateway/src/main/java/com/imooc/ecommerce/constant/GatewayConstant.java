package com.imooc.ecommerce.constant;

/**
 * 网关常量定义
 */
public class GatewayConstant {
    //登录Uri
    public static final String LOGIN_URI = "/e-comerce/login";

    //注册uri
    public static final String REGISTER_URI = "/e-comerce/register";

    //去授权中心登录拿token的uri格式化接口
    public static final String AUTHORITY_CENTER_TOKEN_URI_FORMAT =
            "http://%s:%s/ecommerce-authority-center/authority/token";

    //去授权中心注册拿token的uri格式化接口
    public static  final String AUTHORITY_CENTER_REGISTER_URI_FORMAT =
            "http://%s:%s/ecommerce-authority-center/authority/register";
}
