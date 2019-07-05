package com.alarm.wechat.domain;


import lombok.Getter;
import lombok.Setter;

/**
 * 回复消息类，属性首字母大写转xml(或自行拼xml)
 *
 * @author 酷酷的诚
 */
@Getter
@Setter
public class WechatMessage {

  private String ToUserName;

  private String FromUserName;

  private String CreateTime;

  private String MsgType;

  private String Content;

}
