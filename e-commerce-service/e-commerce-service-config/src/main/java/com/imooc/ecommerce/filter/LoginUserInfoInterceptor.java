package com.imooc.ecommerce.filter;


import com.imooc.ecommerce.constant.CommonConstant;
import com.imooc.ecommerce.util.TokenParseUtil;
import com.imooc.ecommerce.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份统一登录拦截
 */
@SuppressWarnings("all")
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {


    /**
     * 用户请求之前，根据返回判断是否继续或停止执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        //先尝试从 http header 里面拿到 token
        String token = request.getHeader(CommonConstant.JWT_USER_INFO_KEY);

        LoginUserInfo loginUserInfo = null;
        try{
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        }catch (Exception ex){
            log.error("parse login user info error: [{}]",ex.getMessage(),ex);
        }

        //如果程序走到这里 说明Header中没有token信息
        if(null == loginUserInfo){
            throw new RuntimeException("can not parse current login user");
        }

        log.info("set login user info: [{}]",request.getRequestURL());
        //设置当前上下文，把用户信息填充进去
        AccessContext.setLoginUserInfo(loginUserInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在请求完全结束后调用, 常用于清理资源等工作
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        if(null != AccessContext.getLoginUserInfo()){
            AccessContext.clearLoginUserInfo();
        }
    }
}
