package com.alarm.wechat;

import com.alarm.wechat.service.WeChatTicketService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestWeChatTicketService {
  private static Logger log = LoggerFactory.getLogger(TestWeChatTicketService.class);

  @Test
  public void testGetTickket()throws Exception{
    WeChatTicketService weChatTicketService = new WeChatTicketService();
    String result = weChatTicketService.getQrcode("3");
    log.info(result);
  }
}
