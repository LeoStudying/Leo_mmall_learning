package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdAndProductId(@Param(value = "userId") Integer userId,
                                        @Param(value = "productId") Integer productId);
    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteProductByUserIdAndProductIds(@Param(value = "userId") Integer userId, @Param(value = "productList") List<String> productList);

    int checkedOrUncheckedProduct(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId, @Param(value = "checked") Integer checked);

    int getCartProductCount(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}