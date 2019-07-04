package com.alarm.wechat.repository;

import com.alarm.wechat.domain.AlarmMessage;
import com.alarm.wechat.domain.SendUrl;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author 酷酷的诚
 */
public interface AlarmMessageRepository extends JpaRepository<AlarmMessage, Integer> {

  /**
   * 计数预警信息
   *
   * @param sendUrl url
   * @param createtime 时间
   * @param msg 信息
   * @return int
   */
  int countAlarmMessageBySendUrlAndCreatetimeAfterAndMsg(SendUrl sendUrl, long createtime, String msg);
}
