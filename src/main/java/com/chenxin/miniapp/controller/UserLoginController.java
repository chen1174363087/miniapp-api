package com.chenxin.miniapp.controller;

import com.chenxin.miniapp.entity.User;
import com.chenxin.miniapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/miniapp-api/user")
public class UserLoginController {
    private final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login/{openId}", method = RequestMethod.POST)
    public void login(@RequestBody User user, HttpServletRequest request, @PathVariable("openId") String openId) {
        if (jedisCluster.get("token_" + openId) != null && jedisCluster.get("token_" + openId) != "") {
            //存库
            user.setOpenId(openId);
            try {
                userService.login(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //删token 防止重复提交
            jedisCluster.del("token_" + openId);
        }
        logger.info(user.toString());
    }
}
