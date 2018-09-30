package com.qroxy.service.impl;

import com.qroxy.common.Const;
import com.qroxy.common.ServerRespond;
import com.qroxy.common.TokenCache;
import com.qroxy.dao.UserMapper;
import com.qroxy.pojo.User;
import com.qroxy.service.IUserService;
import com.qroxy.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * create by Qroxy
 * *
 * QQ：1114031075
 * *
 * 时间: 2018/9/28-7:03 PM
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
    *@desc:用户登录接口
    *
    *@author:Qroxy
    *
    *@date:2018/9/29 9:09 PM
    *
    *@param:[username, password]
    *
    *@type:com.qroxy.common.ServerRespond<com.qroxy.pojo.User>
    *
    */
    @Override
    public ServerRespond<User> login(String username, String password) {

        int resultCount = userMapper.checkByUsername(username);
        if (resultCount == 0) {
            return ServerRespond.createByErrorMessage("用户名不存在");
        }
//     密码登录md5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerRespond.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerRespond.createBySuccess("登录成功", user);
    }

    /**
     * @desc:用户注册
     * @author:Qroxy
     * @data:2018/9/29 1:39 PM
     * @param:[user]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @Override
    public ServerRespond<String> register(User user) {
//     int resultCount=userMapper.checkByUsername(user.getUsername());
//     if (resultCount>0){
//         return ServerRespond.createByErrorMessage("用户名已存在");
//     }
//   resultCount =userMapper.checkEmail(user.getEmail());
//     if (resultCount>0){
//         return ServerRespond.createByErrorMessage("邮箱已存在");
//     }
//     复用校验代码
        ServerRespond validServerRespond = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validServerRespond.isSuccess()) {
            return validServerRespond;

        }
        validServerRespond = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validServerRespond.isSuccess()) {
            return validServerRespond;

        }
        user.setRole(Const.Role.ROLE_COSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerRespond.createByErrorMessage("注册失败");
        }
        return ServerRespond.createBySuccessMessage("注册成功");
    }

    /**
     * @desc:数据校验
     * @author:Qroxy
     * @data:2018/9/29 1:32 PM
     * @param:[str, type]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @Override
    public ServerRespond<String> checkValid(String str, String type) {

        if (StringUtils.isNotBlank(type)) {
//            开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkByUsername(str);
                if (resultCount > 0) {
                    return ServerRespond.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerRespond.createByErrorMessage("邮箱已存在");
                }
            }

        } else {
            return ServerRespond.createByErrorMessage("参数错误");
        }
        return ServerRespond.createBySuccessMessage("校验成功");
    }

    /**
     * @desc:找回密码
     * @author:Qroxy
     * @date:2018/9/29 3:52 PM
     * @param:[username]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */
    @Override
    public ServerRespond<String> selectQuestion(String username) {
        ServerRespond valid = this.checkValid(username, Const.USERNAME);
        if (valid.isSuccess()) {
            return ServerRespond.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerRespond.createBySuccess(question);
        }
        return ServerRespond.createBySuccessMessage("找回问题密码是空的");


    }

    @Override
    public ServerRespond<String> checkAnswer(String username, String question, String answer) {

        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String token = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, token);
            return ServerRespond.createBySuccess(token);

        }
        return ServerRespond.createByErrorMessage("问题的答案错误");
    }


    /**
     * @desc:重设密码，token校验
     * @author:Qroxy
     * @date:2018/9/29 7:55 PM
     * @param:[username, passwordNew, forgetToken]
     * @type:com.qroxy.common.ServerRespond<java.lang.String>
     */

    @Override
    public ServerRespond<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerRespond.createByErrorMessage("参数错误，token需要传递");
        }
        ServerRespond valid = this.checkValid(username, Const.USERNAME);
        if (valid.isSuccess()) {
            return ServerRespond.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerRespond.createByErrorMessage("Token无效或者过期");
        }
        if (StringUtils.equals(forgetToken, token)) {

            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, MD5Util.MD5EncodeUtf8(md5Password));
            if (rowCount > 0) {
                return ServerRespond.createBySuccessMessage("修改密码成功");
            }

        } else {
            return ServerRespond.createByErrorMessage("token错误，请重新获取重置密码的token");
        }

        return ServerRespond.createBySuccessMessage("修改密码失败");

    }


    @Override
    public ServerRespond<String> resetPassword(String passwordOld, String passwordNew, User user) {
//    防止横向越权，要校验一下用户的旧密码，一定要指定是这个用户，因为我们会查询一个count（1），如果不指定id，那么结果就是true count>0
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerRespond.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerRespond.createBySuccessMessage("密码更新成功");
        }
        return ServerRespond.createByErrorMessage("密码更新失败");

    }

    @Override
    public ServerRespond<User> updateInfomation(User user) {
        //username是不能被更新的
        //email也要进行一个校验，校验新的eamil是否已经存在了，并且存在的email如果相同的话，不能是我们当前这个用户的
        int resultCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount>0){
            return ServerRespond.createByErrorMessage("email已被使用！请更换email再尝试更新。");
        }
        User updateuser=new  User();
        updateuser.setId(user.getId());
        updateuser.setAnswer(user.getAnswer());
        updateuser.setEmail(user.getEmail());
        updateuser.setPhone(user.getPhone());
        updateuser.setQuestion(user.getQuestion());
        int updateCount=userMapper.updateByPrimaryKeySelective(updateuser);
        if (updateCount>0){
            return ServerRespond.createBySuccessMessage("更新用户信息成功");

        }



        return ServerRespond.createByErrorMessage("更新用户信息失败，请重试！");

    }
/**
*@desc:获取用户信息
*
*@author:Qroxy
*
*@date:2018/9/29 9:32 PM
*
*@param:[userId]
*
*@type:com.qroxy.common.ServerRespond<com.qroxy.pojo.User>
*
*/
    @Override
    public ServerRespond<User> getInformation(Integer userId) {
    User user=userMapper.selectByPrimaryKey(userId);
    if (user==null){
        ServerRespond.createByErrorMessage("找不到当前用户信息");
    }
         user.setPassword(StringUtils.EMPTY);
        return ServerRespond.createBySuccess("查询成功",user);
    }
}
