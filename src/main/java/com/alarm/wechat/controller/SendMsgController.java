package com.alarm.wechat.controller;

import com.alarm.wechat.domain.SendUrl;
import com.alarm.wechat.domain.User;
import com.alarm.wechat.repository.SendUrlRepository;
import com.alarm.wechat.service.SendMsgService;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 发送微信消息
 *
 * @author 酷酷的诚
 */
@Controller
public class SendMsgController {

  private static final Logger logger = LoggerFactory.getLogger(SendMsgController.class);

  private final SendMsgService sendMsgService;
  private final SendUrlRepository sendUrlRepository;

  @Autowired
  public SendMsgController(SendMsgService sendMsgService, SendUrlRepository sendUrlRepository) {
    this.sendMsgService = sendMsgService;
    this.sendUrlRepository = sendUrlRepository;
  }

  /**
   * Get请求，发送微信消息
   */
  @RequestMapping(value = "/sendMsg/{param}", method = RequestMethod.GET)
  public void sendMsgGet(@RequestParam(value = "msg") String msg,
      @PathVariable(value = "param") String param, HttpServletResponse response) throws Exception {
    PrintWriter out = response.getWriter();
    try {
      sendMsgService.sendMsg(param, msg);
    } catch (Exception e) {
      logger.error(e.getMessage());
      out.print("{\"success\":false,\"msg\":" + e.getMessage() + "}");
    }
    out.print("{\"success\":true}");
  }

  /**
   * Post请求，发送微信消息
   */
  @RequestMapping(value = "/sendMsg/{param}", method = RequestMethod.POST)
  public void sendMsgPost(@PathVariable(value = "param") String param, HttpServletRequest request, HttpServletResponse response) throws Exception {
    BufferedReader br = request.getReader();
    String str;
    StringBuilder msg = new StringBuilder();
    while ((str = br.readLine()) != null) {
      msg.append(str);
    }
    try {
      sendMsgService.sendMsg(param, msg.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    PrintWriter out = response.getWriter();
    out.print("{\"sucess\",true}");
  }

  /**
   * 前台工具，发送微信消息
   */
  @RequestMapping(value = "/postMsg", method = RequestMethod.POST)
  public String postMsg(@RequestParam(value = "desp") String msg, HttpServletRequest request,
      Model model) throws Exception {
    User user = (User) request.getSession().getAttribute("user");
    SendUrl sendUrl = sendUrlRepository.findByUser(user);
    sendMsgService.sendMsg(sendUrl.getParam(), msg);
    model.addAttribute("sendUrl", sendUrl.getUrl());
    return "send";
  }


}
