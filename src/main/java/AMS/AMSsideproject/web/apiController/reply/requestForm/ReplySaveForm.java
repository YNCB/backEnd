package AMS.AMSsideproject.web.apiController.reply.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplySaveForm {

//    @ApiModelProperty(example = "test", notes = "댓글 제목")
//    @NotBlank(message = "필수 입력값 입니다.")
//    private String title;

    @ApiModelProperty(example = "test", notes = "댓글 내용")
    @NotBlank(message = "필수 입력값 입니다.")
    private String content;

    @ApiModelProperty(example = "null", notes = "상위 댓글 id 입니다. 상위 댓글이 없는 경우 null 입니다.")
    private Long parent_id; //루트일경우에는 null

}
