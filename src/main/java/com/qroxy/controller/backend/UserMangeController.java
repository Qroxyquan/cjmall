package com.qroxy.controller.backend;

import com.qroxy.common.Const;
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
 * @author: Qroxy
 *
 * @QQ：1114031075
 *
 * @时间: 2018/9/29-9:01 PM
 */
@Controller
@RequestMapping("/mange/user/")
public class UserMangeController {
    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login.do",method =RequestMethod.POST)
    @ResponseBody
    public ServerRespond<User> login(HttpSession session,String  password,String username){
        ServerRespond<User> userServerRespond=iUserService.login(username,password);
        if (userServerRespond.isSuccess()){


      User user=  userServerRespond.getData();
      if (user.getRole()==Const.Role.ROLE_ADMIN){
          //说明登录者为管理员
          session.setAttribute(Const.CURRENT_USER,user);
          return userServerRespond;
      }else {
          return  ServerRespond.createByErrorMessage("不是管理员，无法登录");
      }

    }
    return userServerRespond;
    }
}
