package AMS.AMSsideproject.web.apiController.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditForm {

    public Long userId;
    public String nickname;
    public String job;
    public String main_lang;

}
