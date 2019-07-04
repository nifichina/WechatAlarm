package com.alarm.wechat.auth;

import com.alarm.wechat.common.OauthTypes;
import org.scribe.builder.ServiceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 酷酷的诚
 *
 * 注册 GithubApi GithubAbstractOauthService
 */
@Configuration
public class OauthConfig {

  private static final String CALLBACK_URL = "%s/oauth/%s/callback";

  @Value("${oAuth.github.state}") String state;
  @Value("${oAuth.github.clientId}") String githubClientId;
  @Value("${oAuth.github.clientSecret}") String githubClientSecret;
  @Value("${demo.host}") String host;

  @Bean
  public GithubApi githubApi(){
    return new GithubApi(state);
  }

  @Bean
  public AbstractOauthService getGithubOauthService(){
    return new GithubAbstractOauthService(new ServiceBuilder()
        .provider(githubApi())
        .apiKey(githubClientId)
        .apiSecret(githubClientSecret)
        .callback(String.format(CALLBACK_URL, host, OauthTypes.GITHUB))
        .build());
  }
}