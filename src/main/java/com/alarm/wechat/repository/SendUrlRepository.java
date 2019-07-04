package com.alarm.wechat.repository;

import com.alarm.wechat.domain.SendUrl;
import com.alarm.wechat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 酷酷的诚
 */
public interface SendUrlRepository extends JpaRepository<SendUrl, Integer> {

  /**
   * 根据param字段 查询sendURL对象 param是唯一的
   *
   * @param param param字段
   * @return SendUrl
   */
  SendUrl findByParam(String param);

  /**
   * 根据openId字段 查询sendURL对象 openId（微信用户id ）是唯一的
   *
   * @param openId 微信用户id
   * @return SendUrl
   */
  SendUrl findByOpenId(String openId);

  /**
   * 根据User 查询sendURL对象
   *
   * @param user 用户
   * @return SendUrl
   */
  SendUrl findByUser(User user);
}
