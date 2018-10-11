package com.qroxy.service;

import com.qroxy.common.ServerRespond;
import com.qroxy.vo.CartVo;

/**
 * @create by 林镇权
 * *
 * @QQ：1114031075 *
 * @时间: 2018/10/10-12:19 PM
 */
public interface ICartService {
    public ServerRespond add(Integer userId, Integer productId, Integer count);

    public ServerRespond<CartVo> update(Integer userId, Integer productId, Integer count);

    public ServerRespond<CartVo> deleteProductInCart(Integer userId, String productIds);

    public ServerRespond<CartVo> list(Integer userId);

    public ServerRespond<CartVo> selectOrUnSelectAll(Integer userId, Integer checkOrUncheckAllProduct);

    public ServerRespond<CartVo> selectOrUnSelectProduct(Integer userId, Integer productId, Integer checkOrUncheckAllProduct);

    public ServerRespond<Integer> getCartProductCount(Integer userId);
}
