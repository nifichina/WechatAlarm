package com.alarm.wechat.auth;

import com.alarm.wechat.domain.User;
import lombok.Getter;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * 认证
 *
 * @author 酷酷的诚
 */
public abstract class AbstractOauthService implements OAuthService {

  private final OAuthService oAuthService;
  @Getter
  private final String oAuthType;
  @Getter
  private final String authorizationUrl;

  public AbstractOauthService(OAuthService oAuthService, String type) {
    super();
    this.oAuthService = oAuthService;
    this.oAuthType = type;
    this.authorizationUrl = oAuthService.getAuthorizationUrl(null);
  }

  @Override
  public Token getRequestToken() {
    return oAuthService.getRequestToken();
  }

  @Override
  public Token getAccessToken(Token requestToken, Verifier verifier) {
    return oAuthService.getAccessToken(requestToken, verifier);
  }

  @Override
  public void signRequest(Token accessToken, OAuthRequest request) {
    oAuthService.signRequest(accessToken, request);
  }

  @Override
  public String getVersion() {
    return oAuthService.getVersion();
  }

  @Override
  public String getAuthorizationUrl(Token requestToken) {
    return oAuthService.getAuthorizationUrl(requestToken);
  }


  /**
   * 获取用户信息
   *
   * @param accessToken token
   * @return User
   */
  public abstract User getUser(Token accessToken);

}