package com.qroxy.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.dao.OrderMapper;
import com.qroxy.pojo.Order;
import com.qroxy.pojo.User;
import com.qroxy.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;


/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/15-3:29 PM
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    /**
     * @desc:支付宝支付接口
     * @author:Qroxy
     * @date:2018/10/15 6:11 PM
     * @param:[session, orderNo, request]
     * @type:com.qroxy.common.ServerRespond
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerRespond pay(HttpSession session, Long orderNo, HttpServletRequest request) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    /**
     * @desc:支付宝回调接口
     * @author:Qroxy
     * @date:2018/10/16 8:34 PM
     * @param:[request]
     * @type:java.lang.Object
     */
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //验证回调正确性，避免重复通知
        //很重要，看源码
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "usf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerRespond.createByErrorMessage("非法请求，验证不通过，再恶意请求就要报警了！！");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }
        //todo 验证各种数据

        //支付宝规定返回的字符串
        ServerRespond serverRespond = iOrderService.aliCallback(params);
        if (serverRespond.isSuccess()) {
            return Const.alipayCallback.RESPONSE_SUCCESS;
        }
        return Const.alipayCallback.RESPONSE_FAILED;
    }

    /**
     * @desc:轮回查询
     * @author:Qroxy
     * @date:2018/10/16 8:41 PM
     * @param:[session, orderNo]
     * @type:com.qroxy.common.ServerRespond<java.lang.Boolean>
     */
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerRespond<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerRespond serverRespond = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverRespond.isSuccess()) {
            return ServerRespond.createBySuccess(true);
        }
        return ServerRespond.createBySuccess(false);
    }


}
