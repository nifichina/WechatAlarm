package com.alarm.wechat.common;

import static com.alarm.wechat.common.WeChatConstants.MESSAGE_TEXT;

import com.alarm.wechat.domain.WechatMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



/**
 * @author 酷酷的诚
 *
 * 微信信息
 */
public class MessageUtil {
  /**
   * 将接收到的XML格式，转化为Map对象
   * @param request 将request对象，通过参数传入
   * @return 返回转换后的Map对象
   */
  public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
    HashMap<String, String> map = new HashMap<>(16);
    //从dom4j的jar包中，拿到SAXReader对象。
    SAXReader reader = new SAXReader();
    InputStream is = request.getInputStream();
    //从request中，获取输入流
    Document doc = reader.read(is);
    //从reader对象中,读取输入流
    Element root = doc.getRootElement();
    //获取XML文档的根元素
    List<Element> list = root.elements();
    //获得根元素下的所有子节点
    list.forEach(e -> map.put(e.getName(), e.getText()));
    is.close();
    return map;
  }
  private static String textMessageToXml(WechatMessage wechatMessage){
    XStream xstream=new XStream(new DomDriver("UTF-8"));
    xstream.alias("xml", wechatMessage.getClass());
    return xstream.toXML(wechatMessage);
  }

  public static String initText(String toUserName,String fromUserName,String content){
    WechatMessage text=new WechatMessage();
    text.setFromUserName(toUserName);
    text.setToUserName(fromUserName);
    text.setMsgType(MESSAGE_TEXT);
    text.setCreateTime(new Date().toString());
    text.setContent(content);
    return textMessageToXml(text);
  }

}
