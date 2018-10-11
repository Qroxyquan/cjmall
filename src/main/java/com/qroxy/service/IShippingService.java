package com.qroxy.service;

import com.github.pagehelper.PageInfo;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.Shipping;

import javax.servlet.http.HttpSession;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/11-4:42 PM
 */
public interface IShippingService {
    public ServerRespond add(Integer userId, Shipping shipping);
    public ServerRespond delete(Integer userId, Integer shippingId);
    public ServerRespond update(Integer userId, Shipping shipping);
    public ServerRespond select(Integer userId,Integer shippingId);
    public ServerRespond<PageInfo> list(Integer userId,int pageNum,int pageSize);
}
