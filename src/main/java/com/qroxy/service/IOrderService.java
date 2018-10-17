package com.qroxy.service;

import com.qroxy.common.ServerRespond;

import java.util.Map;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/15-3:35 PM
 */
public interface IOrderService {
    public ServerRespond pay(Long orderNo, Integer userId, String path);

    public ServerRespond aliCallback(Map<String, String> params);

    public ServerRespond<Boolean> queryOrderPayStatus(Integer userId, Long orderNo);
}
