package AMS.AMSsideproject.web.apiController.reply.requestForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyEditForm {

    private String title;
    private String content;
}
