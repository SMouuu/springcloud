package com.imooc.ecommerce.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 通过nacos下发动态路由配置,监听nacos中路由配置变更
 */
@Slf4j
@Component
@DependsOn({"gatewayConfig"})
public class DynamicRouteServiceImplByNacos {

    /**
     * nacos配置服务
     */
    private ConfigService configService;

    private final DynamicRouteServiceImpl dynamicRouteService;

    public DynamicRouteServiceImplByNacos(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    /**
     * Bean 在容器中构造完成之后执行init方法
     */
    @PostConstruct
    public void init(){

        log.info("gateway route init....");
        try{
            //初始化nacos 配置客户端
            configService=initConfigService();
            if(configService==null){
                log.error("init config service fail");
                return;
            }

            //通过 nacos config 并指定路由配置路径去获取路由配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTE_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );
            log.info("get current gateway config: [{}]",configInfo);
            List<RouteDefinition> definitionList =
                    JSON.parseArray(configInfo,RouteDefinition.class);
            if(CollectionUtil.isNotEmpty(definitionList)){
                for(RouteDefinition definition:definitionList){
                    log.info("init gateway config: [{}]",definition.toString());
                    dynamicRouteService.addRouteDefinition(definition);
                }
            }
        }catch (Exception ex){
            log.error("gateway route init has some error: [{}]",ex.getMessage(),ex);
        }

        //设置监听器
        dynamiRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID,GatewayConfig.NACOS_ROUTE_GROUP);

    }

    //初始化nacosconfig
    private ConfigService initConfigService(){
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr",GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace",GatewayConfig.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        }catch (Exception ex){
            log.error("init gateway nacos config error: [{}]",ex.getMessage(),ex);
            return null;
        }
    }

    /**
     * 监听nacos 下发的动态路由配置
     * @param dataId
     * @param group
     */
    private void dynamiRouteByNacosListener(String dataId,String group){
        try{
            //给nacos config 客户端增加一个监听器
            configService.addListener(dataId, group, new Listener() {
                //自己提供线程池操作
                @Override
                public Executor getExecutor() {
                    return null;
                }

                /**
                 *监听器收到配置更新
                 * @param configInfo nacos 中最新的配置定义
                 */
                @Override
                public void receiveConfigInfo(String configInfo) {

                    log.info("start to update config: [{}]",configInfo);
                    List<RouteDefinition> definitionList =
                            JSON.parseArray(configInfo,RouteDefinition.class);
                    log.info("update route: [{}]",definitionList.toString());
                    dynamicRouteService.updateList(definitionList);
                }
            });
        }catch (NacosException ex){
            log.error("dynamic update gateway config error: [{}]",ex.getMessage(),ex);

        }
    }
}
