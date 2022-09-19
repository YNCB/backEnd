package AMS.AMSsideproject.web.apiController.post.requestDto;

import lombok.Data;

@Data
public class PostSaveForm {

    private String title;
    private String problem_uri;
    private String content;

    private String type;
    private String language;
    private Integer level;
}
