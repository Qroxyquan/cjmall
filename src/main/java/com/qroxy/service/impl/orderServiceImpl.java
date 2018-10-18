package com.qroxy.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qroxy.common.Const;
import com.qroxy.common.ServerRespond;
import com.qroxy.dao.*;
import com.qroxy.pojo.*;
import com.qroxy.service.IOrderService;
import com.qroxy.util.BigDecimalUtil;
import com.qroxy.util.DateTimeUtil;
import com.qroxy.util.FTPUtil;
import com.qroxy.util.PropertiesUtil;
import com.qroxy.vo.OrderItemVo;
import com.qroxy.vo.OrderProductVo;
import com.qroxy.vo.OrderVo;
import com.qroxy.vo.ShippingVo;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/15-3:35 PM
 */
@Service("iOrderService")
public class orderServiceImpl implements IOrderService {
    private Logger log = LoggerFactory.getLogger(orderServiceImpl.class);
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * @desc:创建订单
     * @author:Qroxy
     * @date:2018/10/18 1:55 PM
     * @param:[userId, shippingId]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond create(Integer userId, Integer shippingId) {

        //从购物车获取数据
        List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);
        //计算订单总价
        ServerRespond serverRespond = this.getCartOrderItem(userId, cartList);
        if (!serverRespond.isSuccess()) {
            return serverRespond;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) serverRespond.getData();
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);
        //生成订单
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return ServerRespond.createByErrorMessage("生成订单错误");

        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerRespond.createByErrorMessage("购物车为空");

        }
        for (OrderItem orderItem : orderItemList) {

            orderItem.setOrderNo(order.getOrderNo());
        }
        //mybatis的批量插入
        orderItemMapper.batchInsert(orderItemList);
        //生成成功，减少产品库存
        this.reduceProductStock(orderItemList);
        //清空一下购物车
        this.cleanCart(cartList);
        //返回给前端包装数据
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
        return ServerRespond.createBySuccess(orderVo);


    }

    /**
     * @desc:取消订单接口
     * @author:Qroxy
     * @date:2018/10/18 3:53 PM
     * @param:[userId, orderNo]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond cancle(Integer userId, Long orderNo) {

        Order order = orderMapper.selectByUserAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerRespond.createByErrorMessage("用户不存在该订单");

        }
        if (Const.OrderStatus.PAID.getCode() == order.getStatus()) {
            return ServerRespond.createByErrorMessage("用户已付款，无法取消！！！");
        }
        Order updateOrder = new Order();
        updateOrder.setStatus(Const.OrderStatus.CANCELED.getCode());
        int rowCount = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (rowCount > 0) {
            return ServerRespond.createBySuccessMessage("取消订单成功");
        }
        return ServerRespond.createBySuccessMessage("取消订单失败！！");
    }

    /**
     * @desc:获取购物车已勾选的订单
     * @author:Qroxy
     * @date:2018/10/18 4:23 PM
     * @param:[userId]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond getOrderCartProduct(Integer userId) {
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车获取数据
        List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);
        ServerRespond serverRespond = this.getCartOrderItem(userId, cartList);
        if (!serverRespond.isSuccess()) {

            return serverRespond;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) serverRespond.getData();
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {

            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItem(orderItem));
        }
        orderProductVo.setImagehost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setProductTotalPrice(payment);
        return ServerRespond.createBySuccess();

    }

    /**
     * @desc:查看订单详情实现
     * @author:Qroxy
     * @date:2018/10/18 4:47 PM
     * @param:[userId, orderNo]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.OrderVo>
     */
    @Override
    public ServerRespond<OrderVo> getOrderDetatil(Integer userId, long orderNo) {

        Order order = orderMapper.selectByUserAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerRespond.createByErrorMessage("用户不存在该订单");

        }
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
        return ServerRespond.createBySuccess(orderVo);

    }

    /**
     * @desc:获取订单列表分页
     * @author:Qroxy
     * @date:2018/10/18 5:10 PM
     * @param:[userId, pageNum, pageSize]
     * @type:com.qroxy.common.ServerRespond<com.github.pagehelper.PageInfo>
     */
    @Override
    public ServerRespond<PageInfo> getOrderlist(Integer userId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVo> orderVoList = assembleOrderVoList(orderList, userId);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return ServerRespond.createBySuccess(pageInfo);


    }

    /**
     * @desc:订单列表封装
     * @author:Qroxy
     * @date:2018/10/18 5:08 PM
     * @param:[orderList, userId]
     * @type:java.util.List<com.qroxy.vo.OrderVo>
     */
    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) {


        List<OrderVo> orderVoList = Lists.newArrayList();
        List<OrderItem> orderItemList = Lists.newArrayList();
        for (Order order : orderList) {
            if (userId == null) {
                //管理员查询不需要userId
                orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.getByOrderNoUserId(order.getOrderNo(), userId);

            }
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    /**
     * @desc:主体包装类实现
     * @author:Qroxy
     * @date:2018/10/18 3:23 PM
     * @param:[order, orderItemList]
     * @type:com.qroxy.vo.OrderVo
     */
    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {

        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatus.codeOf(order.getStatus()).getValue());
        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {

            orderVo.setReceiveName(shipping.getReceiverName());
            orderVo.setShippingVo(this.assmbleShippingVo(shipping));
        }
        orderVo.setPaymentTime(DateTimeUtil.dateToString(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToString(order.getSendTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToString(order.getCloseTime()));
        orderVo.setEndTime(DateTimeUtil.dateToString(order.getEndTime()));
        orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItem(orderItem);
            orderItemVoList.add(orderItemVo);

        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    /**
     * @desc:组装OrderItemVo
     * @author:Qroxy
     * @date:2018/10/18 9:00 PM
     * @param:[orderItem]
     * @type:com.qroxy.vo.OrderItemVo
     */
    private OrderItemVo assembleOrderItem(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCreateTime(DateTimeUtil.dateToString(orderItem.getCreateTime()));
        return orderItemVo;

    }

    /**
     * @desc:组装shippingVo
     * @author:Qroxy
     * @date:2018/10/18 3:28 PM
     * @param:[shipping]
     * @type:com.qroxy.vo.ShippingVo
     */
    private ShippingVo assmbleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
        return shippingVo;
    }

    /**
     * @desc:清空购物车
     * @author:Qroxy
     * @date:2018/10/18 3:09 PM
     * @param:[cartList]
     * @type:void
     */
    private void cleanCart(List<Cart> cartList) {

        for (Cart cartItem : cartList) {

            cartMapper.deleteByPrimaryKey(cartItem.getId());
        }
    }

    /**
     * @desc:更新库存
     * @author:Qroxy
     * @date:2018/10/18 2:28 PM
     * @param:[orderItemList]
     * @type:void
     */
    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {

            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    /**
     * @desc:生成订单
     * @author:Qroxy
     * @date:2018/10/18 1:54 PM
     * @param:[userId, shipping, payment]
     * @type:com.qroxy.pojo.Order
     */
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatus.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.ON_LINE_PAY.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        //发货时间
        //付款等待时间
        int rowCount = orderMapper.insert(order);
        if (rowCount > 0) {
            return order;
        }
        return null;

    }

    /**
     * @desc:订单号的生成规则（非常重要，延伸）
     * @author:Qroxy
     * @date:2018/10/18 1:55 PM
     * @param:[]
     * @type:long
     */
    private long generateOrderNo() {

        //利用时间戳，简单粗暴，并发是订单号可能相同再加一个随机数
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /**
     * @desc:计算订单总价
     * @author:Qroxy
     * @date:2018/10/18 1:49 PM
     * @param:[orderItemList]
     * @type:java.math.BigDecimal
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            //记得更新payment，否则返回0
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;

    }

    private ServerRespond<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList) {

        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {

            return ServerRespond.createByErrorMessage("购物车为空");
        }
        //校验购物车的数据，包括产品的状态和数量
        for (Cart cartItem : cartList) {


            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (Const.productStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return ServerRespond.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
            }
            //校验库存
            if (cartItem.getQuantity() > product.getStock()) {
                return ServerRespond.createByErrorMessage("产品" + product.getStock() + "库存不足");
            }
            orderItem.setUserId(cartItem.getUserId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        return ServerRespond.createBySuccess(orderItemList);
    }


    /**
     * @desc:支付宝支付接口
     * @author:Qroxy
     * @date:2018/10/15 6:07 PM
     * @param:[orderNo, userId, path]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond pay(Long orderNo, Integer userId, String path) {
        Map<String, String> resultMap = Maps.newHashMap();
        Order order = orderMapper.selectByUserAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerRespond.createByErrorMessage("用户没有该订单");

        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("寸金在线商城，订单号：").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
        System.out.println(orderItemList.get(0));
        for (OrderItem orderItem : orderItemList) {

            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName().toString(),

                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(), orderItem.getQuantity());
            goodsDetailList.add(goods);
        }

//        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
//        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
//        // 创建好一个商品后添加至商品明细列表
//        goodsDetailList.add(goods1);
//
//        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
//        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
//        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)

                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

//                关键部分，把生成二维码上传到图片服务器
                File folder = new File(path);
                if (!folder.exists()) {

                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                //替换s占位符
                String QRPath = String.format(path + "/qr-%s.png",
                        response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                //支付宝调用guava生成二维码
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, QRPath);

                File targetFile = new File(path, qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常", e);
                }
                log.info("QRPath:" + QRPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);

                return ServerRespond.createBySuccess(resultMap);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerRespond.createByErrorMessage("支付宝预下单失败");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerRespond.createByErrorMessage("系统异常，预下单状态未知!!!");
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerRespond.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }

    /**
     * @desc: 简单打印应答
     * @author:Qroxy
     * @date:2018/10/15 6:00 PM
     * @param:[response]
     * @type:void
     */
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    /**
     * @desc:回调状态判断
     * @author:Qroxy
     * @date:2018/10/16 8:25 PM
     * @param:[params]
     * @type:com.qroxy.common.ServerRespond
     */
    @Override
    public ServerRespond aliCallback(Map<String, String> params) {

        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerRespond.createByErrorMessage("寸金商场订单，回调忽略");
        }
        if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) {
            return ServerRespond.createBySuccess("支付宝重复调用");
        }
        if (Const.alipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatus.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatFormEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);
        return ServerRespond.createBySuccess();
    }

    /**
     * @desc:订单轮回查询
     * @author:Qroxy
     * @date:2018/10/16 8:40 PM
     * @param:[userId, orderNo]
     * @type:com.qroxy.common.ServerRespond<java.lang.Boolean>
     */
    @Override
    public ServerRespond<Boolean> queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerRespond.createByErrorMessage("用户没有该订单");

        }
        if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) {
            if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) {
                return ServerRespond.createBySuccess();
            }

        }

        return ServerRespond.createByError();
    }


    //backend

    /**
     * @desc:后台查询订单列表接口
     * @author:Qroxy
     * @date:2018/10/18 5:25 PM
     * @param:[pageNum, pageSize]
     * @type:com.qroxy.common.ServerRespond<com.github.pagehelper.PageInfo>
     */
    @Override
    public ServerRespond<PageInfo> manageList(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAllOrder();
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, null);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return ServerRespond.createBySuccess(pageInfo);
    }

    /**
     * @desc:后台查询订单详情页
     * @author:Qroxy
     * @date:2018/10/18 5:40 PM
     * @param:[orderNo]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.OrderVo>
     */
    @Override
    public ServerRespond<OrderVo> manageDetatil(long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {

            return ServerRespond.createByErrorMessage("订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerRespond.createBySuccess(orderVo);
    }

    @Override
    public ServerRespond<PageInfo> manageSearch(Integer userId, long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {

            return ServerRespond.createByErrorMessage("订单不存在");
        }

        List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
        pageInfo.setList(Lists.newArrayList(orderVo));

        return ServerRespond.createBySuccess(pageInfo);

    }

    /**
     * @desc:订单发货实现
     * @author:Qroxy
     * @date:2018/10/18 6:02 PM
     * @param:[orderNo]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @Override
    public ServerRespond<String> manageSendGoods(long orderNo) {


        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            if (order.getStatus() == Const.OrderStatus.PAID.getCode()) {
                order.setStatus(Const.OrderStatus.SHIPPING.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerRespond.createByErrorMessage("发货成功！！");
            }


        }
        return ServerRespond.createByErrorMessage("订单不存在");
    }

}
