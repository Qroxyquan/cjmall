package com.qroxy.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/10-12:42 PM
 */
public class CartVo {
    List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    //是否都勾选
    private Boolean allChecked;
    private String imgeHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImgeHost() {
        return imgeHost;
    }

    public void setImgeHost(String imgeHost) {
        this.imgeHost = imgeHost;
    }
}
