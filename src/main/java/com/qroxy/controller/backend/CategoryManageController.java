package com.qroxy.controller.backend;

import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.User;
import com.qroxy.service.ICategoryService;
import com.qroxy.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: Qroxy
 *
 * @QQ：1114031075
 *
 * @时间: 2018/10/3-7:54 PM
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;
/**
*@desc:添加分类接口
*
*@author:Qroxy
*
*@date:2018/10/3 9:46 PM
*
*@param:[session, categoryName, parentId]
*
*@type:com.qroxy.common.ServerRespond
*
*/
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerRespond addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");

        }
        //校验是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //处理逻辑
           return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServerRespond.createByErrorMessage("无权限处理，您不是管理员！");
        }

    }
    /**
    *@desc:更新品类接口
    *
    *@author:Qroxy
    *
    *@date:2018/10/3 9:45 PM
    *
    *@param:[session, catagoryId, categoryName]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerRespond setCategoryName(HttpSession session, Integer catagoryId,String categoryName){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");

        }
        //校验是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //处理逻辑
            return iCategoryService.updateCategoryName(catagoryId,categoryName);
        }else {
            return ServerRespond.createByErrorMessage("无权限处理！");
        }
    }
    /**
    *@desc:根据分类id获取平级节点信息接口
    *
    *@author:Qroxy
    *
    *@date:2018/10/4 12:18 AM
    *
    *@param:[session, categoryId]
    *
    *@type:com.qroxy.common.ServerRespond
    *
    */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerRespond getChildrenParallerCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");

        }
        //校验是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //处理逻辑
            return iCategoryService.getChildrenParallerCategory(categoryId);

        }else {
            return ServerRespond.createByErrorMessage("无权限处理！");
        }

    }
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerRespond getChildrenAndDeepllerCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");

        }
        //校验是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //处理逻辑
            //查询当前节点的id和递归子节点的id，0->10000->100000
            return iCategoryService.selectCategoryAndChilrenById(categoryId);


        } else {
            return ServerRespond.createByErrorMessage("无权限处理！");
        }
    }
}
