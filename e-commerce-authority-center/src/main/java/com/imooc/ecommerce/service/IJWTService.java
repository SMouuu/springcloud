package com.imooc.ecommerce.service;

import com.imooc.ecommerce.vo.UsernameAndPassword;

/**
 * JWT相关接口定义
 */
public interface IJWTService {

    /**
     * 生成jwt token ,使用默认的超时时间
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String generateToken(String username,String password) throws Exception;

    /**
     * 生成指定超时时间token，单位为天
     * @param username
     * @param password
     * @param expire
     * @return
     * @throws Exception
     */
    String generateToken(String username,String password,int expire) throws Exception;

    /**
     *注册用户并生成token返回
     * @param usernameAndPassword
     * @return token
     * @throws Exception
     */
    String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception;
}
