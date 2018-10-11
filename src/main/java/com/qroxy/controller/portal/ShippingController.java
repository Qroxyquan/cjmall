package com.qroxy.controller.portal;

import com.github.pagehelper.PageInfo;
import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;

import com.qroxy.pojo.Shipping;
import com.qroxy.pojo.User;
import com.qroxy.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @desc：
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/11-4:41 PM
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;

    /**
     * @desc:增加地址接口
     * @author:Qroxy
     * @date:2018/10/11 4:55 PM
     * @param:[session, shipping]
     * @type:com.qroxy.common.ServerRespond
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerRespond add(HttpSession session, Shipping shipping) {

        //使用对象绑定
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iShippingService.add(user.getId(), shipping);

    }

    /**
     * @desc:删除地址成功
     * @author:Qroxy
     * @date:2018/10/11 5:07 PM
     * @param:[session, shippingId]
     * @type:com.qroxy.common.ServerRespond
     */
    @RequestMapping("delete.do")
    @ResponseBody
    public ServerRespond delete(HttpSession session, Integer shippingId) {

        //使用对象绑定
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iShippingService.delete(user.getId(), shippingId);

    }

    /**
     * @desc:更新地址接口
     * @author:Qroxy
     * @date:2018/10/11 5:17 PM
     * @param:[session, shipping]
     * @type:com.qroxy.common.ServerRespond
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerRespond update(HttpSession session, Shipping shipping) {

        //使用对象绑定
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iShippingService.update(user.getId(), shipping);

    }

    /**
     * @desc:搜索地址接口
     * @author:Qroxy
     * @date:2018/10/11 7:57 PM
     * @param:[session, shippingId]
     * @type:com.qroxy.common.ServerRespond
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerRespond select(HttpSession session, Integer shippingId) {

        //使用对象绑定
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        } else {

        }
        return iShippingService.select(user.getId(), shippingId);

    }

    /**
     * @desc:收货地址分页接口
     * @author:Qroxy
     * @date:2018/10/11 7:57 PM
     * @param:[pageNum, pageSize, session]
     * @type:com.qroxy.common.ServerRespond<com.github.pagehelper.PageInfo>
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerRespond<PageInfo> list(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(pageNum, pageSize, user.getId());
    }

}
