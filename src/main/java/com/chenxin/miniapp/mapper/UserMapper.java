package com.chenxin.miniapp.mapper;

import com.chenxin.miniapp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper
@Qualifier(value = "UserMapper")
public interface UserMapper {
    void login(User user) throws Exception;
}
