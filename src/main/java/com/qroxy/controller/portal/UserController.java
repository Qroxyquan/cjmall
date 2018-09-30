package com.qroxy.controller.portal;

import com.qroxy.common.Const;
import com.qroxy.common.ResponseCode;
import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.User;
import com.qroxy.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @create by Qroxy
 * *
 * QQ：1114031075
 * *
 * 时间: 2018/9/28-6:36 PM
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    /**
     * dec:用户登录
     * <p>
     * author:Qroxy
     * <p>
     * data:2018/9/28 6:52 PM
     * <p>
     * param:[username, password, session]
     * <p>
     * type:java.lang.Object
     */
    @Autowired

    private static IUserService iUserService;


//    public static void main(String[] args) {
//        System.out.println(iUserService);
//    }

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<User> login(String username, String password, HttpSession session) {
//        service-mybatis-dao

        ServerRespond<User> result = iUserService.login(username, password);
        if (result.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, result.getData());
        }
        return result;
    }

    /**
     * @desc:登出用户
     * @author:Qroxy
     * @data:2018/9/29 12:42 AM
     * @param:[session]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerRespond.createBySuccessMessage("登出成功");


    }

    /**
     * @desc:用户注册
     * @author:Qroxy
     * @data:2018/9/29 12:59 AM
     * @param:[user]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<String> register(User user) {
        return iUserService.register(user);

    }

    /**
     * @desc:数据校验接口
     * @author:Qroxy
     * @data:2018/9/29 1:49 PM
     * @param:[str, type]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<String> checkVaild(String str, String type) {

        return iUserService.checkValid(str, type);
    }

    /**
     * @desc:用户登录获取信息接口
     * @author:Qroxy
     * @date:2018/9/29 3:27 PM
     * @param:[session]
     * @type:com.qroxy.common.ServerRespond<com.qroxy.pojo.User>
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<User> getUserInfo(HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerRespond.createBySuccess(user);

        }
        return ServerRespond.createByErrorMessage("用户未登录，无法获取当前用户的信息");

    }

    /**
     * @desc:找回密码问题接口
     * @author:Qroxy
     * @date:2018/9/29 3:55 PM
     * @param:[username]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */

    @RequestMapping(value = "for_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<String> forgetGetQuestion(String username) {


        return iUserService.selectQuestion(username);
    }

    /**
     * @desc:检查问题答案接口
     * @author:Qroxy
     * @date:2018/9/29 4:39 PM
     * @param:[username, question, answer]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @RequestMapping(value = "for_check_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<String> forgetCheckAnswer(String username, String question, String answer) {

        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * @desc:重设密码接口
     * @author:Qroxy
     * @date:2018/9/29 7:56 PM
     * @param:[username, passwordNew, forgetToken]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {

        return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
    }
    /**
    *@desc:登录状态重设密码
    *
    *@author:Qroxy
    *
    *@date:2018/9/29 8:11 PM
    *
    *@param:[session, passwordOld, passwordNew]
    *
    *@type:com.qroxy.common.ServerRespond<java.lang.String>
    *
    */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRespond.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }
    /**
    *@desc:用户更新接口
    *
    *@author:Qroxy
    *
    *@date:2018/9/29 8:39 PM
    *
    *@param:[session, user]
    *
    *@type:com.qroxy.common.ServerRespond<com.qroxy.pojo.User>
    *
    */
    @RequestMapping(value = "update_infomation.do", method = RequestMethod.POST)
    @ResponseBody
    public  ServerRespond<User> updateInfomation(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser==null){
            ServerRespond.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());//防止越权
       ServerRespond respond=iUserService.updateInfomation(user);
        if (respond.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,respond.getData());
        }
        return  respond;
    }
    /**
    *@desc:获取用户信息接口
    *
    *@author:Qroxy
    *
    *@date:2018/9/29 9:32 PM
    *
    *@param:[session]
    *
    *@type:com.qroxy.common.ServerRespond<com.qroxy.pojo.User>
    *
    */
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespond<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerRespond.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
}
}