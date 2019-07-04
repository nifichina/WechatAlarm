package com.alarm.wechat.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.lang.NonNull;


/**
 * 微信token工具
 *
 * @author 酷酷的诚
 */
public class AccessTokenUtil {

  private static final String TOKEN_KEY = "token";

  private LoadingCache<String, String> tokenCache;

  private static class SingletonHolder {

    private static AccessTokenUtil instance = new AccessTokenUtil();
  }

  public static AccessTokenUtil getAccessTokenUtil() {
    return SingletonHolder.instance;
  }

  private AccessTokenUtil() {
    super();
    if (SingletonHolder.instance != null) {
      throw new IllegalStateException();
    }
    tokenCache = CacheBuilder.newBuilder()
        .maximumSize(1)
        .expireAfterAccess(1, TimeUnit.HOURS)
        .build(
            new CacheLoader<String, String>() {
              @Override
              public String load(@NonNull String key) throws Exception {
                return refreshAccessToken();
              }
            });
  }

  public String getAccessToken() throws ExecutionException {
    return tokenCache.get(TOKEN_KEY);
  }

  private String refreshAccessToken() throws Exception {
    String requestUrl = WeChatConfig.getAccessTokenUrl().replace("APPID", WeChatConfig.getAppid()).replace("APPSECRET", WeChatConfig.getAppsecret());
    return HttpClientUtils.get(requestUrl).getString("access_token");
  }
}
