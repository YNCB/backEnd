package AMS.AMSsideproject.web.custom.security.filter.form;

import lombok.Data;

@Data
public class UserLoginForm {

    private String email;
    private String password;
}
