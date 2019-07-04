package com.alarm.wechat.config.intercepors;

import com.alarm.wechat.domain.User;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 登陆拦截
 *
 * @author 酷酷的诚
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

  @Value("${demo.host}")
  String host;

  /**
   * 访问接口之前执行，验证登陆状态的业务逻辑，在用户调用指定接口之前验证登陆状态
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //使用最简单的Session提取User来验证登陆。
    HttpSession session = request.getSession();
    //User是登陆时放入session的
    User user = (User) session.getAttribute("user");
    //如果session中没有user，表示没登陆
    if (user == null) {
      response.sendRedirect(host + "/login");
      return false;
    } else {
      //如果session里有user，表示该用户已经登陆，放行
      return true;
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
  }
}
