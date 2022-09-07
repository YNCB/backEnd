package AMS.AMSsideproject.web.apiController.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class requestUserEdit {

    private Long userId;
    private String nickName;
}
