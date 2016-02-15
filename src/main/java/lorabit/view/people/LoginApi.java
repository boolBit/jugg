package lorabit.view.people;

import com.google.common.collect.Lists;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lorabit.security.Authorities;
import lorabit.security.CurrentUser;
import lorabit.security.ISessionManager;
import lorabit.security.LoginUtil;
import lorabit.security.SessionManager;
import lorabit.security.UserInfo;

import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author lorabit
 * @since 16-2-15
 */
@Controller
public class LoginApi {
  @Resource
  SessionManager sessionManager;


  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    return "login";
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @ResponseBody
  public Map dologin(HttpServletRequest req, HttpServletResponse resp,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String username) {
    Map ret = new HashMap();
    if (isEmpty(password) || isEmpty(username)) {
      ret.put("error", "用户名和密码不能为空");
      return ret;
    }
    if ("lorabit".equals(username) && "123456".equals(password)) {
      ret.put("success", "you hava logined");
      CurrentUser user = new CurrentUser();
      user.setIsApp(false);
      user.setUserInfo(new UserInfo(123l, "lorabit"));
      user.setIsValid(true);
      user.setCookieAge(ISessionManager.DEFAULT_COOKIE_AGE);
      user.setGrantedAuthorities(Lists.<GrantedAuthority>newArrayList(new Authorities.Common()));
      sessionManager.loginAndSave(resp, user);
      return ret;
    }
    ret.put("error", "用户名和密码错误");
    return ret;
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  @ResponseBody
  public String logout(HttpServletResponse resp) {
    sessionManager.logoutAndPurge(LoginUtil.getCurrentUser(), resp);
    return "logout";
  }

}
