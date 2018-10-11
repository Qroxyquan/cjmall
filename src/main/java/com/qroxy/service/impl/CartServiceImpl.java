package com.qroxy.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.dao.CartMapper;
import com.qroxy.dao.ProductMapper;
import com.qroxy.pojo.Cart;
import com.qroxy.pojo.Product;
import com.qroxy.service.ICartService;

import com.qroxy.util.BigDecimalUtil;

import com.qroxy.vo.CartProductVo;
import com.qroxy.vo.CartVo;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/10-12:19 PM
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * @desc:新增商品到购物车实现
     * @author:Qroxy
     * @date:2018/10/10 3:51 PM
     * @param:[userId, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */

    @Override
    public ServerRespond<CartVo> add(Integer userId, Integer productId, Integer count) {
        if (productId == null || userId == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个产品不在这个购物车里，需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        } else {
            //这个产品在这个购物车里，数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        CartVo cartVo = this.getCartVoList(userId);
        return ServerRespond.createBySuccess(cartVo);
    }

    /**
     * @desc:购物车更新实现
     * @author:Qroxy
     * @date:2018/10/10 3:59 PM
     * @param:[userId, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */

    @Override
    public ServerRespond<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || userId == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        CartVo cartVo = this.getCartVoList(userId);
        return ServerRespond.createBySuccess(cartVo);
    }

    /**
     * @desc:删除商品实现
     * @author:Qroxy
     * @date:2018/10/10 4:19 PM
     * @param:[userId, productIds]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */

    @Override
    public ServerRespond<CartVo> deleteProductInCart(Integer userId, String productIds) {
//        强大的guava提供的工具，使传过来的string值转化为数组
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)) {
            return ServerRespond.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        cartMapper.deleteByUserIdProductId(userId, productList);
        CartVo cartVo = this.getCartVoList(userId);
        return ServerRespond.createBySuccess(cartVo);
    }

    /**
     * @desc:查询购物车集合实现
     * @author:Qroxy
     * @date:2018/10/10 4:25 PM
     * @param:[userId]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @Override
    public ServerRespond<CartVo> list(Integer userId) {
        CartVo cartVo = this.getCartVoList(userId);
        return ServerRespond.createBySuccess(cartVo);
    }

    /**
     * @desc:全选或全不选
     * @author:Qroxy
     * @date:2018/10/10 4:40 PM
     * @param:[userId, checkOrUncheckAllProduct]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @Override
    public ServerRespond<CartVo> selectOrUnSelectAll(Integer userId, Integer checked) {
        cartMapper.checkOrUncheckAllProduct(userId, checked);
        return this.list(userId);
    }

    /**
     * @desc:商品选中或反选
     * @author:Qroxy
     * @date:2018/10/10 4:49 PM
     * @param:[userId, productId, checkOrUncheckAllProduct]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @Override
    public ServerRespond<CartVo> selectOrUnSelectProduct(Integer userId, Integer productId, Integer checked) {
        cartMapper.checkOrUncheckProduct(userId, productId, checked);
        return this.list(userId);
    }

    /**
     * @desc:获取购物车商品数量
     * @author:Qroxy
     * @date:2018/10/10 6:00 PM
     * @param:[userId]
     * @type:com.qroxy.common.ServerRespond<java.lang.Integer>
     */
    @Override
    public ServerRespond<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerRespond.createBySuccess(0);
        }
        return ServerRespond.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

    /**
     * @desc:获取购物车商品封装类
     * @author:Qroxy
     * @date:2018/10/10 3:40 PM
     * @param:[userId]
     * @type:com.qroxy.vo.CartVo
     */

    private CartVo getCartVoList(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByserId(userId);

        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal carTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitNum = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
//                        库存充足的时候
                        buyLimitNum = cartItem.getQuantity();

                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitNum = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
//                        购物车更新有效库存
                        Cart carFortQuantity = new Cart();
                        carFortQuantity.setId(cartItem.getId());
                        carFortQuantity.setQuantity(buyLimitNum);
                        cartMapper.updateByPrimaryKeySelective(carFortQuantity);

                    }
                    cartProductVo.setQuantity(buyLimitNum);
//                    计算总价
                    cartProductVo.setProductTotalPriceTotal(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());

                }
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    //如果已经勾选，增加到整个购物车总价当中
                    carTotalPrice = BigDecimalUtil.add(carTotalPrice.doubleValue(), cartProductVo.getProductPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(carTotalPrice);
        cartVo.setAllChecked(this.getAllChecked(userId));
        cartVo.setImgeHost("ftp.server.http.prefix");
        return cartVo;
    }

    /**
     * @desc:判断是否全选状态
     * @author:Qroxy
     * @date:2018/10/10 3:36 PM
     * @param:[userId]
     * @type:boolean
     */
    private boolean getAllChecked(Integer userId) {
        if (userId == null) {
            return false;
        } else {
            return cartMapper.selectCartProdutCheckedStatusByUserId(userId) == 0;
        }
    }
}
