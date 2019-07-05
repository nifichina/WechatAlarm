package com.alarm.wechat.controller;

import com.alarm.wechat.auth.AbstractOauthService;
import com.alarm.wechat.auth.OauthServices;
import com.alarm.wechat.domain.SendUrl;
import com.alarm.wechat.domain.User;
import com.alarm.wechat.repository.SendUrlRepository;
import com.alarm.wechat.repository.UserRepository;
import com.alarm.wechat.service.WeChatTicketService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 酷酷的诚
 */
@Controller
public class AccountController {

  @Value("${demo.host}")
  String host;
  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
  private final OauthServices oAuthServices;
  private final UserRepository userRepository;
  private final SendUrlRepository sendUrlRepository;
  private final WeChatTicketService weChatTicketService;

  @Autowired
  public AccountController(OauthServices oAuthServices, UserRepository userRepository, SendUrlRepository sendUrlRepository, WeChatTicketService weChatTicketService) {
    this.oAuthServices = oAuthServices;
    this.userRepository = userRepository;
    this.sendUrlRepository = sendUrlRepository;
    this.weChatTicketService = weChatTicketService;
  }

  @RequestMapping(value = {"", "/login"}, method = RequestMethod.GET)
  public String showLogin(HttpServletRequest request, Model model) {
    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");
    if (user == null) {
      model.addAttribute("oAuthServices", oAuthServices.getAbstractOauthServices());
      return "login";
    } else {
      return getReturnString(model, user);
    }
  }

  /**
   * github 回调
   */
  @RequestMapping(value = "/oauth/{type}/callback", method = RequestMethod.GET)
  public String callback(@RequestParam(value = "code") String code,
      @PathVariable(value = "type") String type,
      HttpServletRequest request, Model model) {
    AbstractOauthService oAuthService = oAuthServices.getOauthService(type);
    Token accessToken = oAuthService.getAccessToken(null, new Verifier(code));
    User oAuthInfo = oAuthService.getUser(accessToken);
    User user = userRepository.findByOauthTypeAndOauthId(oAuthInfo.getOauthType(),
        oAuthInfo.getOauthId());
    if (user == null) {
      model.addAttribute("oAuthInfo", oAuthInfo);
      user = userRepository.save(oAuthInfo);
    }
    request.getSession().setAttribute("user", user);
    return "redirect:/";
  }

  @RequestMapping(value = "/redirectSend", method = RequestMethod.GET)
  public String redirectSend(HttpServletRequest request, Model model) {
    User user = (User) request.getSession().getAttribute("user");
    SendUrl sendUrl = sendUrlRepository.findByOauthId(user.getOauthId());
    if (null == sendUrl) {
      model.addAttribute("flag", "false");
      return "index";
    } else {
      model.addAttribute("sendUrl", sendUrl.getUrl());
      return "send";
    }
  }

  private String getReturnString(Model model, User user) {
    SendUrl sendUrl = sendUrlRepository.findByOauthId(user.getOauthId());
    if (null == sendUrl) {
      try {
        model.addAttribute("ticketUrl", weChatTicketService.getQrcode(user.getId().toString()));
      } catch (Exception e) {
        logger.error("获取微信二维码异常" + e.getMessage());
        return "login";
      }
      return "index";
    } else {
      model.addAttribute("sendUrl", sendUrl.getUrl());
      return "send";
    }
  }
}