package com.chenxin.miniapp.service.impl;

import com.chenxin.miniapp.entity.User;
import com.chenxin.miniapp.mapper.UserMapper;
import com.chenxin.miniapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    @Qualifier(value = "UserMapper")
    private UserMapper userMapper;

    @Override
    public void login(User user) throws Exception {
        userMapper.login(user);
    }

    @Override
    public Integer haveUser(String openId) throws Exception {
        return userMapper.haveUser(openId);
    }

    @Override
    public List<HashMap> listUser() throws Exception {
        return userMapper.listUser();
    }

    @Override
    public List<User> listUserPojo() throws Exception {
        return userMapper.listUserPojo();
    }

    @Override
    public User getUser(String table) throws Exception {
        return userMapper.getUser(table);
    }
}
