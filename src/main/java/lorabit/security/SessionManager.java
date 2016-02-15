package lorabit.security;

import com.google.common.base.Preconditions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lorabit
 * @since 16-2-14
 */
public class SessionManager extends AbstractPreAuthenticatedProcessingFilter implements ISessionManager {


    private ObjectMapper mapper = new ObjectMapper();

    @Resource
    private StringRedisTemplate redisTemplate;


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        CurrentUser user = (CurrentUser) authResult.getPrincipal();
        if (user.isValid() && user.isApp()) {
            user.setCookieAge(14 * 24 * 60 * 60);
            loginAndSave(response, user);
        }
        super.successfulAuthentication(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        Object obj = request.getAttribute(SESSIONID);
        if (obj != null && obj instanceof SessionId) {
            SessionId sid = (SessionId) obj;
            response.addCookie(mkCookie(-1, SESSIONID, sid.getSessionId()));
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        CurrentUser user = new CurrentUser();
        loadSession(user, request);
        if (!user.isValid()) {
            tryLoginInApp(user, request);
        }
        if (!user.isValid()) {
            request.setAttribute(SESSIONID, user.getSessionId());
        }
        return user;
    }

    private void loadSession(CurrentUser user, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (SESSIONID.equals(cookie.getName())) {
                user.setSessionId(new SessionId(cookie.getValue()));
                Map<String, Object> session = getSessionFromRedis(cookie.getValue());
                if (session.get(UID) == null) break;
                long userId = Long.valueOf(String.valueOf(session.get(UID)));
                String username = (String) session.get(UNAME);
                user.setUserInfo(UserInfo.create(userId, username));
                user.setIsValid(true);
                return;
            }
        }
        if (user.getSessionId() == null)
            user.setSessionId(SessionId.generate());
    }

    private void tryLoginInApp(CurrentUser user, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (AUTH_TOKEN.equals(cookie.getName())) {
                //根据token 到数据库中查找信息
                user.setIsApp(true);
                return;
            }
        }
    }

    private Map<String, Object> getSessionFromRedis(String name) {
        Map<String, Object> map = new HashMap<>();
        try {
            String sessionInfo = findRedisSessionData(name);
            Preconditions.checkArgument(sessionInfo != null);
            Preconditions.checkArgument(sessionInfo.split(":").length >= 2);
            return mapper.readValue(sessionInfo.substring(sessionInfo.indexOf(":") + 1), map.getClass());
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }

    private String findRedisSessionData(String name) {
        return redisTemplate.opsForValue().get(name);
    }


    @Override
    public void loginAndSave(HttpServletResponse resp, CurrentUser user) {
        user.setSessionId(SessionId.generate());
        saveSession(user);
        resp.addCookie(mkCookie(user.getCookieAge(), UID, String.valueOf(user.getUserInfo().getUserId())));
        resp.addCookie(mkCookie(user.getCookieAge(), UNAME, user.getUsername()));
        resp.addCookie(mkCookie(user.getCookieAge(), SESSIONID, user.getSessionId().getSessionId()));
    }


    public Cookie mkCookie(int age, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(DOMAIN);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        return cookie;
    }


    public void saveSession(CurrentUser user) {
        Map<String, Object> map = new HashMap();
        map.put(UID, user.getUserInfo().getUserId());
        map.put(UNAME, user.getUsername());
        String info = null;
        try {
            info = "lorabit:" + mapper.writeValueAsString(map);
            redisTemplate.opsForValue().set(user.getSessionId().getSessionId(), info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "";
    }

    public void logoutAndPurge(CurrentUser user, HttpServletResponse resp) {
        if(user !=null && user.getSessionId()!=null){
            delValue(user.getSessionId().getSessionId());
        }
        resp.addCookie(purgeCookie(UID));
        resp.addCookie(purgeCookie(UNAME));
        resp.addCookie(purgeCookie(SESSIONID));
    }

    public Cookie purgeCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setDomain(DOMAIN);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }


    public void delValue(String name) {
        try {
            redisTemplate.delete(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putValue(String key, String value, long ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
