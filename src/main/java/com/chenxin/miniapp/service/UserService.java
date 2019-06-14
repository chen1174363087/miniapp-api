package com.chenxin.miniapp.service;


import com.chenxin.miniapp.entity.User;

public interface UserService {
    void login(User user) throws Exception;

    Integer haveUser(String openId) throws Exception;
}
