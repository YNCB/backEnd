package AMS.AMSsideproject.web.exception.security;

import org.springframework.security.core.AuthenticationException;

public class LoginUserInternalException extends AuthenticationException {

    public LoginUserInternalException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LoginUserInternalException(String msg) {
        super(msg);
    }
}
