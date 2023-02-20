package AMS.AMSsideproject.web.exception.JWT;

import org.springframework.security.core.AuthenticationException;

public class TokenExistingException extends AuthenticationException {
    public TokenExistingException(String message) {
        super(message);
    }
}
