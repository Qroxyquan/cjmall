package com.qroxy.controller.backend;

import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.Product;
import com.qroxy.pojo.User;
import com.qroxy.service.IProductService;
import com.qroxy.service.IUserService;
import com.qroxy.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/5-4:14 PM
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    /**
    *@desc:增加或更新产品接口
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 4:37 PM
    *
    *@param:[session, product]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerRespond productSave(HttpSession session, Product product){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage("用户未登录，请先登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }
    /**
    *@desc:更新产品销售转台接口
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 4:51 PM
    *
    *@param:[session, productId, status]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerRespond setSaleStatus(HttpSession session, Integer productId,Integer status){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //更新产品销售状态
            return iProductService.setSaleStatus(productId,status);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }
    /**
    *@desc:产品详情接口
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 9:20 PM
    *
    *@param:[session, productId]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerRespond getdetail(HttpSession session, Integer productId){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.manageProductDetail(productId);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }
    /**
    *@desc:获取商品列表接口（分页显示）
    *
    *@author:Qroxy
    *
    *@date:2018/10/5 9:58 PM
    *
    *@param:[session, pageSize, pageNum]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerRespond getList(HttpSession session, @RequestParam(value = "pageSize",defaultValue ="1") int pageSize, @RequestParam(value = "pageNum",defaultValue ="10") int pageNum){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.getProductList(pageNum,pageSize);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerRespond searchList(HttpSession session, String productName,Integer productId, @RequestParam(value = "pageSize",defaultValue ="1") int pageSize, @RequestParam(value = "pageNum",defaultValue ="10") int pageNum){


        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return  ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员！");

        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);

        }else {
            return  ServerRespond.createByErrorMessage("无权限操作！");
        }
    }











}
