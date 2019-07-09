package com.chenxin.miniapp.service;


import com.chenxin.miniapp.entity.User;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    void login(User user) throws Exception;

    Integer haveUser(String openId) throws Exception;

    List<HashMap> listUser() throws Exception;

    List<User> listUserPojo() throws Exception;

    /**
     * 动态表名
     *
     * @param table
     * @return
     * @throws Exception
     */
    User getUser(String table) throws Exception;


}
