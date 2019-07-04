package com.alarm.wechat.auth;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author 酷酷的诚
 */
@Service
public class OauthServices {

  @Getter
  private final List<AbstractOauthService> abstractOauthServices;

  @Autowired
  public OauthServices(List<AbstractOauthService> abstractOauthServices) {
    this.abstractOauthServices = abstractOauthServices;
  }

  public AbstractOauthService getOauthService(String type){
    Optional<AbstractOauthService> oAuthService = abstractOauthServices.stream().filter(o -> o.getOAuthType().equals(type))
        .findFirst();
    return oAuthService.orElse(null);
  }

}