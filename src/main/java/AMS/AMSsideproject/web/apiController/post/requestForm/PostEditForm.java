package AMS.AMSsideproject.web.apiController.post.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEditForm {

    @ApiModelProperty(example = "DFS", notes = "문제 태그들 여러개 가능. 없을시 빈배열([])로 주시기 바랍니다.")
    private List<String> tags;

    @ApiModelProperty(example = "test")
    @NotBlank(message = "필수 입력값 입니다.")
    private String title;

    @ApiModelProperty(example = "test", notes = "없을시 null로 채워주시면 됩니다.")
    private String problem_uri;

    @ApiModelProperty(example = "test")
    @NotBlank(message = "필수 입력값 입니다.")
    private String content;

    @ApiModelProperty(example = "see", notes = "see, alone")
    @NotBlank(message = "필수 입력값 입니다.")
    private String type;

    @ApiModelProperty(example = "Java")
    @NotBlank(message = "필수 입력값 입니다.")
    private String language;

    @ApiModelProperty(example = "3")
    @NotBlank(message = "필수 입력값 입니다.")
    private Integer level;
}
