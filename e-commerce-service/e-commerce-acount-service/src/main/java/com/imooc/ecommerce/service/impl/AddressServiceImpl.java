package com.imooc.ecommerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.account.AddressInfo;
import com.imooc.ecommerce.common.TableId;
import com.imooc.ecommerce.dao.EcommerceAddressDao;
import com.imooc.ecommerce.entity.EcommerceAddress;
import com.imooc.ecommerce.filter.AccessContext;
import com.imooc.ecommerce.service.IAddressService;
import com.imooc.ecommerce.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户地址相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AddressServiceImpl implements IAddressService {

    private final EcommerceAddressDao addressDao;

    public AddressServiceImpl(EcommerceAddressDao addressDao) {
        this.addressDao = addressDao;
    }

    /**
     * 存储多个地址信息
     */
    @Override
    public TableId createAddressInfo(AddressInfo addressInfo) {

        //不能直接从参数中获取用户的 id 信息
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        //将传递的参数转换为实体对象（生产环境需要校验）
        List<EcommerceAddress> ecommerceAddressList = addressInfo.getAddressItems().stream()
                .map(a -> EcommerceAddress.to(loginUserInfo.getId(), a))
                .collect(Collectors.toList());

        //保存到数据表并把返回记录的id 给调用方
        List<EcommerceAddress> savedRecords = addressDao.saveAll(ecommerceAddressList);
        List<Long> ids = savedRecords.stream()
                .map(EcommerceAddress::getId).collect(Collectors.toList());
        log.info("create address info:[{}], [{}]",loginUserInfo.getId(),
                JSON.toJSONString(ids));
        return new TableId(
                ids.stream().map(TableId.Id::new).collect(Collectors.toList())
        );
    }

    @Override
    public AddressInfo getCurrentAddressInfo() {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        //根据 userId 查询到用户的地址信息，在实现转换
        List<EcommerceAddress> ecommerceAddressList = addressDao.findAllByUserId(
                loginUserInfo.getId()
        );
        List<AddressInfo.AddressItem> addressItems = ecommerceAddressList.stream()
                .map(EcommerceAddress::toAddressItem)
                .collect(Collectors.toList());
        return new AddressInfo(loginUserInfo.getId(), addressItems);
    }

    @Override
    public AddressInfo getAddressInfoById(Long id) {
        EcommerceAddress ecommerceAddress = addressDao.findById(id).orElse(null);
        if (null == ecommerceAddress) {
            throw new RuntimeException("address is not exist");
        }

        return new AddressInfo(
                ecommerceAddress.getUserId(),
                Collections.singletonList(ecommerceAddress.toAddressItem())
        );
    }

    @Override
    public AddressInfo getAddressInfoByTableId(TableId tableId) {

        List<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId).collect(Collectors.toList());
        log.info("get address info by table id: [{}]", JSON.toJSONString(ids));

        List<EcommerceAddress> ecommerceAddresses = addressDao.findAllById(ids);
        if (CollectionUtils.isEmpty(ecommerceAddresses)) {
            return new AddressInfo(-1L, Collections.emptyList());
        }

        List<AddressInfo.AddressItem> addressItems = ecommerceAddresses.stream()
                .map(EcommerceAddress::toAddressItem)
                .collect(Collectors.toList());

        return new AddressInfo(
                ecommerceAddresses.get(0).getUserId(), addressItems
        );
    }
}
