package com.imooc.ecommerce.controller;

import com.imooc.ecommerce.account.AddressInfo;
import com.imooc.ecommerce.common.TableId;
import com.imooc.ecommerce.service.IAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户地址服务 controller
 */

@Api(tags= "用户地址服务")
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    private final IAddressService addressService;

    public AddressController(IAddressService addressService){
        this.addressService = addressService;
    }

    //vlue 是简述 ，notes 是详细的描述信息
    @ApiOperation(value = "创建",notes = "创建用户地址信息", httpMethod = "POST")
    @PostMapping("/create-address")
    public TableId createAddressInfo(@RequestBody AddressInfo addressInfo){

        return addressService.createAddressInfo(addressInfo);
    }

    @ApiOperation(value = "当前用户", notes = "获取当前登录用户地址信息", httpMethod = "GET")
    @GetMapping("/current-address")
    public AddressInfo getCurrentAddressInfo() {
        return addressService.getCurrentAddressInfo();
    }

    @ApiOperation(value = "获取用户地址信息",
            notes = "通过 id 获取用户地址信息, id 是 EcommerceAddress 表的主键",
            httpMethod = "GET")
    @GetMapping("/address-info")
    public AddressInfo getAddressInfoById(@RequestParam Long id) {
        return addressService.getAddressInfoById(id);
    }

    @ApiOperation(value = "获取用户地址信息",
            notes = "通过 TableId 获取用户地址信息", httpMethod = "POST")
    @PostMapping("/address-info-by-table-id")
    public AddressInfo getAddressInfoByTablesId(@RequestBody TableId tableId) {
        return addressService.getAddressInfoByTableId(tableId);
    }
}
