package com.chenxin.miniapp.service.impl.UserServiceImpl;

import com.chenxin.miniapp.entity.User;
import com.chenxin.miniapp.mapper.UserMapper;
import com.chenxin.miniapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    @Qualifier(value = "UserMapper")
    private UserMapper userMapper;

    @Override
    public void login(User user) throws Exception {
        userMapper.login(user);
    }
}
