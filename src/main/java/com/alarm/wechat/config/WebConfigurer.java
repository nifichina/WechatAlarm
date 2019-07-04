package com.alarm.wechat.config;

import com.alarm.wechat.config.intercepors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author 酷酷的诚
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

  private final LoginInterceptor loginInterceptor;

  @Autowired
  public WebConfigurer(LoginInterceptor loginInterceptor) {
    this.loginInterceptor = loginInterceptor;
  }

  /**
   * 配置静态资源比如html，js，css，等等
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");
  }

  /**
   * 注册拦截器
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/sendMsg/", "/oauth/{type}/callback", "/weChat/chat");


  }
}