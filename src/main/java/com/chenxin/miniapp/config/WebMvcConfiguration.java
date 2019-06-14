package com.chenxin.miniapp.config;

import com.chenxin.miniapp.interceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    @Qualifier(value = "interceptor")
    private Interceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(interceptor.getClass());
        registry.addInterceptor(interceptor).addPathPatterns("/miniapp-api/**").excludePathPatterns("/miniapp-api/initToken");
    }
}
