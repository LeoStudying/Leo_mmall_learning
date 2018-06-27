package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * 描述:
 * IProductService
 *
 * @outhor Leo
 * @create 2018-06-24 下午 4:10
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);
    
    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse mangeProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(Integer pageNumber, Integer pageSize);

    ServerResponse<PageInfo> searchProduct(Integer productId, String productName, Integer pageNumber,Integer pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);
}