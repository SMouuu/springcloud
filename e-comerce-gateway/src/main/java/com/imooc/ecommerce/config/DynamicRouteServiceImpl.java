package com.imooc.ecommerce.config;


import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 事件推送 Aware：动态更新路由网关 Service
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    //写路由定义
    private final RouteDefinitionWriter routeDefinitionWriter;

    //获取路由定义
    private final RouteDefinitionLocator routeDefinitionLocator;

    //事件发布
    private ApplicationEventPublisher publisher;

    public DynamicRouteServiceImpl(RouteDefinitionWriter routeDefinitionWriter,
                                   RouteDefinitionLocator routeDefinitionLocator){
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.routeDefinitionWriter = routeDefinitionWriter;
    }
    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        //完成事件推送句柄初始化
        this.publisher = applicationEventPublisher;


    }

    /**
     * 增加路由定义
     * @param routeDefinition
     * @return
     */
    public String addRouteDefinition(RouteDefinition definition){
        log.info("gateway add route: [{}]",definition);

        //保存路由配置并发布
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();

        //发布时间通知给gateway，同步新增路由定义
        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        return "sucess";
    }

    public String updateList(List<RouteDefinition> definitions){
        log.info("gateway update route: [{}]",definitions);

        //先拿到gateway中存储的路由定义
        List<RouteDefinition> routeDefinitionsExits = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if(!CollectionUtil.isEmpty(routeDefinitionsExits)){
            //清除掉所有旧的路由定义
            routeDefinitionsExits.forEach(rd -> {
                log.info("delete route definition: [{}]",rd);
                deleteById(rd.getId());
            });
        }

        //把更新的路由定义同步到gateway中
        definitions.forEach(definition -> updateByRouteDefinition(definition));
        return "success";
    }

    /**
     * 根据路由id删除路由配置
     * @param id
     * @return
     */
    private String deleteById(String id){
        try{
            log.info("gateway delete route id: [{}]",id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            //发布时间通知给gateway，更新路由定义
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete sucess";
        }catch (Exception ex){
            log.error("gateway delete route fail: [{}]",ex.getMessage(),ex);
            return "delete fail";
        }

    }

    /**
     * 更新路由，更新实现策略：删除+新增=更新
     * @param definition
     * @return
     */
    private String updateByRouteDefinition(RouteDefinition definition){

        try{
            log.info("gateway update route: [{}]",definition);
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        }catch (Exception ex){
            return "update fail,not find route routeId: "+definition.getId();
        }
        try{
            this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "sucess";
        }catch (Exception ex){
            return "update route fail";
        }

    }

}













