package com.chenxin.miniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/miniapp-api")
public class InitTokenController {
    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping(value = "/initToken", method = RequestMethod.GET)
    public Map<String,Object> initToken(@RequestParam("openId") String openId) {
        Map<String,Object> result = new HashMap<>();
        String token = UUID.randomUUID().toString();
        jedisCluster.set("token_"+openId,token);
        result.put("token",token);
        return result;
    }
}
