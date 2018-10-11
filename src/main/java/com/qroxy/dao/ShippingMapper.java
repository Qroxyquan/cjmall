package com.qroxy.dao;

import com.qroxy.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    int deleteByShippingIdAndUserId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);
    int updateByUserId(Shipping record);
    Shipping selectByShippingIdUserId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);
    List<Shipping> selectByUserId(Integer userId);
}