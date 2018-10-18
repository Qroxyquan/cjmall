package com.qroxy.service;

import com.github.pagehelper.PageInfo;
import com.qroxy.common.ServerRespond;
import com.qroxy.vo.OrderVo;

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

    public ServerRespond create(Integer userId, Integer shippingId);

    public ServerRespond cancle(Integer userId, Long orderNo);

    public ServerRespond getOrderCartProduct(Integer userId);

    public ServerRespond<OrderVo> getOrderDetatil(Integer userId, long orderNo);

    public ServerRespond<PageInfo> getOrderlist(Integer userId, Integer pageNum, Integer pageSize);

    //backend
    public ServerRespond<PageInfo> manageList(int pageNum, int pageSize);

    public ServerRespond<OrderVo> manageDetatil(long orderNo);

    public ServerRespond<PageInfo> manageSearch(Integer userId, long orderNo, int pageNum, int pageSize);

    public ServerRespond<String> manageSendGoods(long orderNo);
}
