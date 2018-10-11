package com.qroxy.controller.portal;

import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.User;
import com.qroxy.service.ICartService;
import com.qroxy.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/10-10:41 AM
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    /**
     * @desc:添加到购物车接口
     * @author:Qroxy
     * @date:2018/10/10 3:46 PM
     * @param:[session, userId, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerRespond<CartVo> add(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.add(user.getId(), productId, count);

    }

    /**
     * @desc:购物车更新接口
     * @author:Qroxy
     * @date:2018/10/10 3:59 PM
     * @param:[session, userId, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerRespond<CartVo> update(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.update(user.getId(), productId, count);

    }

    /**
     * @desc:删除购物车商品接口
     * @author:Qroxy
     * @date:2018/10/10 4:20 PM
     * @param:[session, productIds]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerRespond<CartVo> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.deleteProductInCart(user.getId(), productIds);

    }

    /**
     * @desc:购物车查询接口
     * @author:Qroxy
     * @date:2018/10/10 4:27 PM
     * @param:[session]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerRespond<CartVo> add(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.list(user.getId());

    }

    /**
     * @desc:全选接口
     * @author:Qroxy
     * @date:2018/10/10 4:29 PM
     * @param:[session, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerRespond<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.selectOrUnSelectAll(user.getId(), Const.Cart.CHECKED);

    }

    /**
     * @desc:取消全选接口
     * @author:Qroxy
     * @date:2018/10/10 4:29 PM
     * @param:[session, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("unselect_all.do")
    @ResponseBody
    public ServerRespond<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.selectOrUnSelectAll(user.getId(), Const.Cart.UNCHECKED);

    }

    /**
     * @desc:单个商品选中接口
     * @author:Qroxy
     * @date:2018/10/10 4:29 PM
     * @param:[session, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("select_product.do")
    @ResponseBody
    public ServerRespond<CartVo> selectProduct(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.selectOrUnSelectProduct(user.getId(), productId, Const.Cart.CHECKED);

    }

    /**
     * @desc:单个商品反选接口
     * @author:Qroxy
     * @date:2018/10/10 4:29 PM
     * @param:[session, productId, count]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.vo.CartVo>
     */
    @RequestMapping("unselect_product.do")
    @ResponseBody
    public ServerRespond<CartVo> unSelectProduct(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iCartService.selectOrUnSelectProduct(user.getId(), productId, Const.Cart.UNCHECKED);

    }

    /**
     * @desc:获取购物车商品总数量
     * @author:Qroxy
     * @date:2018/10/11 11:25 AM
     * @param:[session]
     * @type:com.qroxy.common.ServerRespond<java.lang.Integer>
     */
    @RequestMapping("get_cat_product_count.do")
    @ResponseBody
    public ServerRespond<Integer> getCatProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createBySuccess(0);
        } else {

        }
        return iCartService.getCartProductCount(user.getId());

    }

}
