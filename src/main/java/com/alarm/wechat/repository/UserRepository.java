package com.alarm.wechat.repository;

import com.alarm.wechat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author 酷酷的诚
 */
public interface UserRepository extends JpaRepository<User, Integer> {


  /**
   * 根据GitHub认证信息查询用户
   *
   * @param oAuthId github id
   * @param oAuthType github type
   * @return User
   */
  User findByOauthTypeAndOauthId(String oAuthType, String oAuthId);

}
