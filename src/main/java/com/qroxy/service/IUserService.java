package com.qroxy.service;

import com.qroxy.common.ServerRespond;
import com.qroxy.pojo.User;

/**
 * create by Qroxy
 * *
 * QQ：1114031075
 * *
 * 时间: 2018/9/28-7:01 PM
 */

public interface IUserService {
    ServerRespond<User> login(String username, String password);

    public ServerRespond<String> register(User user);

    public ServerRespond<String> checkValid(String str, String type);

    public ServerRespond<String> selectQuestion(String username);

    public ServerRespond<String> checkAnswer(String username, String question, String answer);

    public ServerRespond<String> forgetRestPassword(String username, String passwordNew, String forgetToken);

    public ServerRespond<String> resetPassword(String passwordOld, String passwordNew, User user);
    public ServerRespond<User> updateInfomation(User user);
    public ServerRespond<User> getInformation(Integer userId);
    ServerRespond checkAdminRole(User user);



}
