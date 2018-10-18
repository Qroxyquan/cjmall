package com.qroxy.dao;

import com.qroxy.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);


    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("priductId") Integer priductId);

    List<Cart> selectCartByserId(Integer userId);

    int selectCartProdutCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductId(@Param("userId") Integer userId, @Param("priductIdList") List<String> priductIdList);

    int checkOrUncheckAllProduct(@Param("userId") Integer userId, @Param("checked") Integer checked);

    int checkOrUncheckProduct(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    int selectCartProductCount(Integer userId);

    List<Cart> selectCheckedByUserId(Integer userId);
}