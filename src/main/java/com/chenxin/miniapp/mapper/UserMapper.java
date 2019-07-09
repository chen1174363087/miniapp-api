package com.chenxin.miniapp.mapper;

import com.chenxin.miniapp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.List;

@Mapper
@Qualifier(value = "UserMapper")
public interface UserMapper {
    void login(User user) throws Exception;

    Integer haveUser(@Param("openId") String openId) throws Exception;

    List<HashMap> listUser() throws Exception;
    List<User> listUserPojo() throws Exception;

    @Select("select * from ${table}")//${}占位符
    User getUser(@Param("table") String table) throws Exception;

    List<User> userListByResultMap() throws Exception;
}
