package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * 描述:
 * IShippingService
 *
 * @outhor Leo
 * @create 2018-07-08 下午 2:05
 */
public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> selectShippingByUserIdAndShippingId(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> selectShippingByUserId(Integer userId, Integer pageNum, Integer pageSize);

}