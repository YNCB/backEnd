package AMS.AMSsideproject.web.apiController.post.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveForm {

    private List<String> tags;

    private String title;
    private String problem_uri;
    private String content;

    private String type;
    private String language;
    private Integer level;
}
