package AMS.AMSsideproject.web.apiController.post.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveForm {

    @ApiModelProperty(example = "DFS", notes = "문제 태그들 여러개 가능")
    private List<String> tags;

    @ApiModelProperty(example = "test")
    private String title;
    @ApiModelProperty(example = "test")
    private String problem_uri;

    @ApiModelProperty(example = "test")
    private String content;

    @ApiModelProperty(example = "see", notes = "see, alone")
    private String type;
    @ApiModelProperty(example = "Java")
    private String language;
    @ApiModelProperty(example = "3")
    private Integer level;
}
