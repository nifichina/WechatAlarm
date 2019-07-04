package com.alarm.wechat.domain;


import lombok.Getter;
import lombok.Setter;

/**
 * @author 酷酷的诚
 */
@Getter
@Setter
public class WechatMessage {

  private String toUserName;

  private String fromUserName;

  private String createTime;

  private String msgType;

  private String content;

}
