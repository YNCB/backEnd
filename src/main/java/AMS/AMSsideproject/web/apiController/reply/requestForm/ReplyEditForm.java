package AMS.AMSsideproject.web.apiController.reply.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyEditForm {

    @ApiModelProperty(example = "test", notes = "댓글 제목")
    @NotBlank(message = "필수 입력값 입니다.")
    private String title;

    @ApiModelProperty(example = "test", notes = "댓글 제목")
    @NotBlank(message = "필수 입력값 입니다.")
    private String content;
}
