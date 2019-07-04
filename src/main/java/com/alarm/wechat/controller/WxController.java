package com.alarm.wechat.controller;

import com.alarm.wechat.common.AesException;
import com.alarm.wechat.common.HttpClientUtils;
import com.alarm.wechat.common.MessageUtil;
import com.alarm.wechat.common.Sha1;
import com.alarm.wechat.common.WeChatConstants;
import com.alarm.wechat.domain.SendUrl;
import com.alarm.wechat.domain.User;
import com.alarm.wechat.repository.SendUrlRepository;
import com.alarm.wechat.repository.UserRepository;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 微信验证接通
 *
 * @author 酷酷的诚
 */
@Controller
@RequestMapping("/weChat")
public class WxController {

  private static Logger log = LoggerFactory.getLogger(WxController.class);
  @Value("${demo.host}")
  String host;
  @Value("${wechat.config.token}")
  String token;
  private final UserRepository userRepository;
  private final SendUrlRepository sendUrlRepository;

  @Autowired
  public WxController(UserRepository userRepository, SendUrlRepository sendUrlRepository) {
    this.userRepository = userRepository;
    this.sendUrlRepository = sendUrlRepository;
  }

  @RequestMapping(value = "/chat", method = {RequestMethod.GET, RequestMethod.POST}, produces = HttpClientUtils.MEDIA_TYPE_CHARSET_JSON_UTF8)
  public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //如果为get请求，则为开发者验证模式
    if (HttpClientUtils.GET.equals(request.getMethod().toLowerCase())) {
      doGet(request, response);
    } else {
      PrintWriter out = response.getWriter();
      String message = "OK";
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      try {
        Map<String, String> map = MessageUtil.xmlToMap(request);
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");
        request.getSession().setAttribute("openid", fromUserName);
        String msgType = map.get("MsgType");
        String eventKey = map.get("EventKey");
        if (msgType.equals(WeChatConstants.MESSAGE_EVENT)) {
          //从集合中，获取是哪一种事件传入
          String eventType = map.get("Event");
          if (WeChatConstants.TEMPLATESENDJOBFINISH.equals(eventType)) {
            return;
          }
          //扫描带参数的二维码，如果用户未关注，则可关注公众号，事件类型为subscribe；用户已关注，则事件类型为SCAN
          else if (eventType.equals(WeChatConstants.MESSAGE_SUBSCRIBE)) {
            sendUrlRepository.save(buildSendUrl(eventKey, fromUserName));
          } else if (eventType.equals(WeChatConstants.MESSAGE_SCAN)) {
            sendUrlRepository.save(buildSendUrl(eventKey, fromUserName));
          } else if (eventType.equals(WeChatConstants.MESSAGE_UNSUBSCRIBE)) {
            //取消关注
            sendUrlRepository.delete(sendUrlRepository.findByOpenId(fromUserName));
          }
        }
        //返回转换后的XML字符串
        out.print(MessageUtil.initText(toUserName, fromUserName, message));
      } catch (DocumentException e) {
        log.error(e.getMessage());
      }
    }
  }

  private SendUrl buildSendUrl(String eventKey, String fromUserName) {
    //对获取到的参数进行处理
    String[] params = eventKey.split("_");
    String userid = "";
    String useridkey = "userid";
    int two = 2, three = 3;
    if (params.length == two) {
      log.info("二维码参数为-----name:" + params[0] + ",code:" + params[1]);
      if (useridkey.equalsIgnoreCase(params[0])) {
        userid = params[1];
      }
    }
    if (params.length == three) {
      log.info("二维码参数为-----name:" + params[0] + ",code:" + params[1]);
      if (useridkey.equalsIgnoreCase(params[1])) {
        userid = params[2];
      }
    }
    Optional<User> optionalUser = userRepository.findById(Integer.valueOf(userid));
    SendUrl sendUrl = new SendUrl();
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      sendUrl.setUser(user);
      sendUrl.setParam(user.getOauthId());
      sendUrl.setOpenId(fromUserName);
      sendUrl.setFlag(true);
      sendUrl.setUrl(host + "/sendMsg/" + user.getOauthId() + "?msg=");
      return sendUrl;
    }
    return null;
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response) {
    String signature = request.getParameter("signature");
    String timestamp = request.getParameter("timestamp");
    String nonce = request.getParameter("nonce");
    String echoStr = request.getParameter("echostr");
    String secret = "";
    try {
      //这里是对三个参数进行加密
      secret = Sha1.getSha1(token, timestamp, nonce, "");
    } catch (AesException e) {
      log.error(e.getMessage());
    }
    PrintWriter out;
    try {
      out = response.getWriter();
    } catch (IOException e) {
      log.error(e.getMessage());
      return;
    }
    if (secret.equals(signature)) {
      out.print(echoStr);
    }
    out.close();
  }
}

