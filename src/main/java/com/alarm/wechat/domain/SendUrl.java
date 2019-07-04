package com.alarm.wechat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;


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
  @Unique
  @OneToOne
  private User user;
  private String param;
  private String url;
  private String openId;
  private boolean flag;
}
