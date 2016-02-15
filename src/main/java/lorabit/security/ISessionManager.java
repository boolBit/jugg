package lorabit.security;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lorabit
 * @since 16-2-15
 */
public interface ISessionManager {

    static final String SESSIONID = "session_id";
    static final String UID = "u_id";
    static final String UNAME = "u_name";
    static final String AUTH_TOKEN = "auth_token";
    static final String DOMAIN = "localhost";

    static final int DEFAULT_COOKIE_AGE = 24 * 60 * 60;
    static final int REMEMBER_ME_COOKIE_AGE = 14 * 24 * 60 * 60;


    void loginAndSave(HttpServletResponse resp, CurrentUser user);
}
