package lorabit.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author lorabit
 * @since 16-2-15
 */
public class Authorities {

    public static class Common implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return "COMMON_USER";
        }
    }
}
