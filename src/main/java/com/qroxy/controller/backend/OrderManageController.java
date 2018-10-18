package com.qroxy.controller.backend;

import com.github.pagehelper.PageInfo;
import com.qroxy.common.Const;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.PayInfo;
import com.qroxy.pojo.User;
import com.qroxy.service.IOrderService;
import com.qroxy.service.IUserService;
import com.qroxy.vo.OrderVo;
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
 * @时间: 2018/10/18-5:14 PM
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iorderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerRespond list(HttpSession session,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage("用户未登录，请先登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充增加产品的业务逻辑
            return iorderService.manageList(pageNum, pageSize);

        } else {
            return ServerRespond.createByErrorMessage("无权限操作！");
        }


    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerRespond<OrderVo> listDetail(HttpSession session, long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage("用户未登录，请先登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充增加产品的业务逻辑
            return iorderService.manageDetatil(orderNo);

        } else {
            return ServerRespond.createByErrorMessage("无权限操作！");
        }


    }

    /**
     * @desc:后台产品搜索接口
     * @author:Qroxy
     * @date:2018/10/18 5:57 PM
     * @param:[session, orderNo, pageNum, pageSize]
     * @type:com.qroxy.common.ServerRespond<com.github.pagehelper.PageInfo>
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerRespond<PageInfo> search(HttpSession session, long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage("用户未登录，请先登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充增加产品的业务逻辑
            return iorderService.manageSearch(user.getId(), orderNo, pageNum, pageSize);

        } else {
            return ServerRespond.createByErrorMessage("无权限操作！");
        }


    }

    /**
     * @desc:订单发货接口
     * @author:Qroxy
     * @date:2018/10/18 6:04 PM
     * @param:[session, orderNo]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerRespond<String> sendGoods(HttpSession session, long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage("用户未登录，请先登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充增加产品的业务逻辑
            return iorderService.manageSendGoods(orderNo);

        } else {
            return ServerRespond.createByErrorMessage("无权限操作！");
        }


    }

}
