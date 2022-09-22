package com.imooc.ecommerce.advice;


import com.imooc.ecommerce.annotation.IgnoreResponseAdvice;
import com.imooc.ecommerce.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 实现统一响应
 */
@RestControllerAdvice(value="org.example")
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object>{

    /**
     * 判断是否需要对响应进行处理
     */
    @Override
    @SuppressWarnings("all")//警告信息屏蔽
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }
        if(methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o, MethodParameter returnType,
                                  MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //定义最终的返回对象
        CommonResponse<Object> response=new CommonResponse<>(0,"");
        if(null==o){
            return response;
        }else if(o instanceof CommonResponse){
            response = (CommonResponse<Object>) o;
        }else{
            response.setData(o);
        }
        return null;
    }
}
