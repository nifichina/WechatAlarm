package com.alarm.wechat.repository;

import com.alarm.wechat.domain.AlarmMessage;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author 酷酷的诚
 */
public interface AlarmMessageRepository extends JpaRepository<AlarmMessage, Integer> {

  /**
   * 计数预警信息
   *
   * @param openId 微信用户ID
   * @param createtime 时间
   * @param msg 信息
   * @return int
   */
  int countAlarmMessageByOpenIdAndCreatetimeAfterAndMsg(String openId, long createtime, String msg);
}
