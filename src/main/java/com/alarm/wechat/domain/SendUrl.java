package com.alarm.wechat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;


/**
 * @author 酷酷的诚
 */
@Entity
@Getter
@Setter
public class SendUrl {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String oauthId;
  private String url;
  private String openId;
}
