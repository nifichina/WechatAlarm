package com.alarm.wechat.repository;

import com.alarm.wechat.domain.SendUrl;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 酷酷的诚
 */
public interface SendUrlRepository extends JpaRepository<SendUrl, Integer> {

  /**
   * 根据oauthId字段 查询sendURL对象 oauthId是唯一的
   *
   * @param oauthId github oauth id
   * @return SendUrl
   */
  SendUrl findByOauthId(String oauthId);

  /**
   * 根据openId字段 查询sendURL对象 openId（微信用户id ）是唯一的
   *
   * @param openId 微信用户id
   * @return SendUrl
   */
  SendUrl findByOpenId(String openId);


}
