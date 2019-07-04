package com.alarm.wechat.service;

import com.alarm.wechat.common.AccessTokenUtil;
import com.alarm.wechat.common.HttpClientUtils;
import com.alarm.wechat.domain.AlarmMessage;
import com.alarm.wechat.domain.SendUrl;
import com.alarm.wechat.repository.AlarmMessageRepository;
import com.alarm.wechat.repository.SendUrlRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author 酷酷的诚
 */
@Service
public class SendMsgService {

  @Value("${wechat.config.templateId}")
  String templateId;

  private final SendUrlRepository sendUrlRepository;
  private final AlarmMessageRepository alarmMessageRepository;

  @Autowired
  public SendMsgService(SendUrlRepository sendUrlRepository, AlarmMessageRepository alarmMessageRepository) {
    this.sendUrlRepository = sendUrlRepository;
    this.alarmMessageRepository = alarmMessageRepository;
  }

  public void sendMsg(String param, String msg) throws Exception {
    SendUrl sendUrl = sendUrlRepository.findByParam(param);
    LocalDateTime localDateTime = LocalDateTime.now();
    long createtime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    int count = alarmMessageRepository.countAlarmMessageBySendUrlAndCreatetimeAfterAndMsg(sendUrl, createtime - 300000, msg);
    if (count > 0) {
      //5 min 不发重复预警信息
      return;
    }
    AlarmMessage alarmMessage = new AlarmMessage();
    alarmMessage.setCreatetime(createtime);
    alarmMessage.setSendUrl(sendUrl);
    alarmMessage.setMsg(msg);
    alarmMessageRepository.save(alarmMessage);
    String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String sendBody = "{\"touser\":\"$1\",\"template_id\":\"$2\",\"data\":{\"first\":{\"value\":\"$3\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"$4\",\"color\":\"#173177\"}}}";
    String body = sendBody.replace("$1", sendUrl.getOpenId()).replace("$2", templateId).replace("$3", msg).replace("$4", date);
    String accessToken = AccessTokenUtil.getAccessTokenUtil().getAccessToken();
    HttpClientUtils.postWechatMsg(accessToken, body);
  }
}
