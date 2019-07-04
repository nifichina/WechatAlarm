package com.alarm.wechat.common;


/**
 * 不同的消息类型
 *
 * @author 酷酷的诚
 */
public class WeChatConstants {

  /**
   * 文本消息
   */
  static final String MESSAGE_TEXT = "text";
  /**
   * 事件推送
   */
  public static final String MESSAGE_EVENT = "event";
  /**
   * 关注
   */
  public static final String MESSAGE_SUBSCRIBE = "subscribe";
  /**
   * 取消关注
   */
  public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
  /**
   * 扫描带参数二维码——未关注时触发subscribe已关注时触发scan
   */
  public static final String MESSAGE_SCAN = "SCAN";
  /**
   * 模板消息送达情况提醒
   */
  public static final String TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";

}
