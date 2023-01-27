package AMS.AMSsideproject.web.apiController.post.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEditForm {

    @ApiModelProperty(example = "[]", notes = "없을시 빈배열로 주시면 됩니다.")
    @NotNull(message = "필수 입렵값 입니다.")
    private List<String> tags;

    @ApiModelProperty(example = "test")
    @NotBlank(message = "필수 입력값 입니다.")
    private String title;

    @ApiModelProperty(example = "test", notes = "없을시 빈값으로 채워주시면 됩니다.")
    @NotNull(message = "필수 입력값 입니다.")
    private String problem_uri;

    @ApiModelProperty(example = "test")
    @NotBlank(message = "필수 입력값 입니다.")
    private String content;

    @ApiModelProperty(example = "SEE", notes = "SEE, ALONE")
    @NotBlank(message = "필수 입력값 입니다.")
    @Pattern(regexp = "SEE|ALONE", message = "SEE,ALONE 중 하나입니다.")
    private String type;

    @ApiModelProperty(example = "Java")
    @NotBlank(message = "필수 입력값 입니다.")
    @Pattern(regexp = "C|C++|C#|Python|JS|Java|Swift|Kotlin|Ruby|Swift|Go|Etc",
            message = "C++/Python/JS/Java/C/C#/Swift/Kotlin/Ruby/Swift/Go/Etc 중 하나입니다.")
    private String language;

    @ApiModelProperty(example = "3", notes = "1,2,3,4,5")
    @NotNull(message = "필수 입력값 입니다.")
    @Max(value = 5, message = "최대 5입니다.")
    @Min(value = 1, message = "최소 1입니다.")
    private Integer level;
}
