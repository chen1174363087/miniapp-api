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
                //是否由用户有信息
                if (userService.haveUser(openId) > 0) {
                    logger.info("用户已存在！");
                } else {
                    userService.login(user);
                    //删token 防止重复提交
                    jedisCluster.del("token_" + openId);
                }
            } catch (Exception e) {
                logger.error("用户登陆异常", e);
            }
        }
        logger.info(user.toString());
    }
}
