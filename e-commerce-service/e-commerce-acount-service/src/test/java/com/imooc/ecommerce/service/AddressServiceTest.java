package com.imooc.ecommerce.service;

import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.account.AddressInfo;
import com.imooc.ecommerce.common.TableId;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

/**
 * 用户地址相关服务功能测试
 */
@Slf4j
public class AddressServiceTest extends BaseTest{

    @Autowired
    private IAddressService addressService;

    /**
     * 测试创建用户信息
     */
    @Test
    public void testCreateAddressInfo(){

        AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();
        addressItem.setUsername("shen");
        addressItem.setPhone("19999898989");
        addressItem.setProvince("上海市");
        addressItem.setCity("上海市");
        addressItem.setAddressDetail("陆家嘴");

        log.info("test create address info: [{}]", JSON.toJSONString(
                addressService.createAddressInfo(
                        new AddressInfo(loginUserInfo.getId(), Collections.singletonList(addressItem))
                )
        ));

    }

    /**
     * 测试获取当前登录用户信息
     */
    @Test
    public void testGetCurrentAddressInfo(){

        log.info("test get current user info: [{}]",JSON.toJSONString(
                addressService.getCurrentAddressInfo()
        ));
    }

    @Test
    public void testGetAddressInfoById(){
        log.info("test get address info by id: [{}]",JSON.toJSONString(
                addressService.getAddressInfoById(1L)
        ));
    }

    @Test
    public void testGetAddressInfoByTableId(){

        log.info("test get address info by table id: [{}]",JSON.toJSONString(
                addressService.getAddressInfoByTableId(
                        new TableId(Collections.singletonList(new TableId.Id(1L)))
                )
        ));
    }
}
