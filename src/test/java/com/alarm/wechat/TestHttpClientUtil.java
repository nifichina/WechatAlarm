package com.alarm.wechat;

import com.alarm.wechat.common.AccessTokenUtil;
import com.alarm.wechat.common.HttpClientUtils;
import java.net.SocketTimeoutException;
import org.apache.http.conn.ConnectTimeoutException;
import org.junit.Test;


public class TestHttpClientUtil {


  @Test
  public void test() {
    try {
      String str = HttpClientUtils.post("https://localhost:443/ssl/test.shtml", "name=12&page=34", "application/x-www-form-urlencoded", "UTF-8", 10000, 10000);
      //String str= get("https://localhost:443/ssl/test.shtml?name=12&page=34","GBK");
            /*Map<String,String> map = new HashMap<String,String>();
            map.put("name", "111");
            map.put("page", "222");
            String str= postForm("https://localhost:443/ssl/test.shtml",map,null, 10000, 10000);*/
      System.out.println(str);
    } catch (ConnectTimeoutException e) {
      e.printStackTrace();
    } catch (SocketTimeoutException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPostWechatQrcode() throws Exception{
    String access_token = AccessTokenUtil.getAccessTokenUtil().getAccessToken();
    String  body = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"userID_3\"}}}";
    System.out.println(HttpClientUtils.postWechatQrcode(access_token,body));
  }
}
