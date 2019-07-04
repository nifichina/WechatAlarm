package com.alarm.wechat.common;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONObject;
import org.springframework.http.MediaType;


/**
 * http 工具  包括向微信发送信息
 *
 * @author 酷酷的诚
 */
public class HttpClientUtils {

  public final static String MEDIA_TYPE_CHARSET_JSON_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";CHARSET=UTF-8";
  public final static String GET = "get";
  private static final int CONN_TIMEOUT = 10000;
  private static final int READ_TIMEOUT = 10000;
  private static final String CHARSET = "UTF-8";
  private static HttpClient client;
  private static final String HTTPS = "https";

  static {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(128);
    cm.setDefaultMaxPerRoute(128);
    client = HttpClients.custom().setConnectionManager(cm).build();
  }

  /**
   * 获取微信公众号二维码
   */
  public static String postWechatQrcode(String accessToken, String body) throws Exception {
    String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
    return post(url, body, "application/json", CHARSET, CONN_TIMEOUT, READ_TIMEOUT);
  }

  public static void postWechatMsg(String accessToken, String body) throws Exception {
    String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
    String mimeTyppe = "application/json";
    post(url, body, mimeTyppe, CHARSET, CONN_TIMEOUT, READ_TIMEOUT);
  }

  /**
   * 发送一个 Post 请求, 使用指定的字符集编码.
   *
   * @param url url
   * @param body RequestBody
   * @param mimeType 例如 application/xml "application/x-www-form-urlencoded" a=1&b=2&c=3
   * @param charset 编码
   * @param connTimeout 建立链接超时时间,毫秒.
   * @param readTimeout 响应超时时间,毫秒.
   * @return 结果.
   * @throws ConnectTimeoutException 建立链接超时异常
   * @throws SocketTimeoutException 响应超时
   * @throws Exception 其他异常
   */
  public static String post(String url, String body, String mimeType, String charset, Integer connTimeout, Integer readTimeout)
      throws ConnectTimeoutException, SocketTimeoutException, Exception {
    HttpClient client = null;
    HttpPost post = new HttpPost(url);
    String result;
    try {
      if (StringUtils.isNotBlank(body)) {
        HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
        post.setEntity(entity);
      }
      // 设置参数
      Builder customReqConf = RequestConfig.custom();
      if (connTimeout != null) {
        customReqConf.setConnectTimeout(connTimeout);
      }
      if (readTimeout != null) {
        customReqConf.setSocketTimeout(readTimeout);
      }
      post.setConfig(customReqConf.build());

      HttpResponse res;
      if (url.startsWith(HTTPS)) {
        // 执行 Https 请求.
        client = createSslInsecureClient();
        res = client.execute(post);
      } else {
        // 执行 Http 请求.
        client = HttpClientUtils.client;
        res = client.execute(post);
      }
      result = IOUtils.toString(res.getEntity().getContent(), charset);
    } finally {
      post.releaseConnection();
      if (url.startsWith(HTTPS) && client instanceof CloseableHttpClient) {
        ((CloseableHttpClient) client).close();
      }
    }
    return result;
  }


  /**
   * 发送一个 GET 请求
   *
   * @param url url
   * @return 结果.
   * @throws ConnectTimeoutException 建立链接超时
   * @throws SocketTimeoutException 响应超时
   * @throws Exception 其他异常
   */
  static JSONObject get(String url)
      throws ConnectTimeoutException, SocketTimeoutException, Exception {

    HttpClient client = null;
    HttpGet get = new HttpGet(url);
    String result;
    try {
      // 设置参数
      Builder customReqConf = RequestConfig.custom();
      get.setConfig(customReqConf.build());
      HttpResponse res;
      if (url.startsWith(HTTPS)) {
        // 执行 Https 请求.
        client = createSslInsecureClient();
        res = client.execute(get);
      } else {
        // 执行 Http 请求.
        client = HttpClientUtils.client;
        res = client.execute(get);
      }
      result = IOUtils.toString(res.getEntity().getContent(), StandardCharsets.UTF_8.toString());
    } finally {
      get.releaseConnection();
      if (url.startsWith(HTTPS) && client instanceof CloseableHttpClient) {
        ((CloseableHttpClient) client).close();
      }
    }
    return new JSONObject(result);
  }

  /**
   * 创建 SSL连接
   */
  private static CloseableHttpClient createSslInsecureClient() throws GeneralSecurityException {
    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();

    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

      @Override
      public boolean verify(String arg0, SSLSession arg1) {
        return true;
      }

      @Override
      public void verify(String host, SSLSocket ssl) {
      }

      @Override
      public void verify(String host, X509Certificate cert) {
      }

      @Override
      public void verify(String host, String[] cns,
          String[] subjectAlts) {
      }

    });

    return HttpClients.custom().setSSLSocketFactory(sslsf).build();

  }

}