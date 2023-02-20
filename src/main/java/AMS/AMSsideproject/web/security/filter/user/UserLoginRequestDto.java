package AMS.AMSsideproject.web.security.filter.user;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String email;
    private String password;
}
