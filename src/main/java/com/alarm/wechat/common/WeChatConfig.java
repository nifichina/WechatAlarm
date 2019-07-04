package com.alarm.wechat.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置
 *
 * @author 酷酷的诚
 */
@Configuration
public class WeChatConfig {

  @Getter
  private static String appid;
  @Getter
  private static String appsecret;
  @Getter
  private static String accessTokenUrl;

  @Autowired
  public void setAppid(@Value("${wechat.config.appid}") String appid) {
    WeChatConfig.appid = appid;
  }

  @Autowired
  public void setAppsecret(@Value("${wechat.config.appsecret}") String appsecret) {
    WeChatConfig.appsecret = appsecret;
  }

  @Autowired
  public void setAccessTokenUrl(@Value("${wechat.config.accessTokenUrl}")
      String accessTokenUrl) {
    WeChatConfig.accessTokenUrl = accessTokenUrl;
  }
}
