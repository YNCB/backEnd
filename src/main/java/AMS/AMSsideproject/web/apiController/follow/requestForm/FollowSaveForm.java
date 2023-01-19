package AMS.AMSsideproject.web.apiController.follow.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowSaveForm {

    @ApiModelProperty(example = "1", notes = "팔로우할 사용자 Id")
    @NotNull(message = "필수 입력값 입니다.")
    private Long userId; //팔로우할 유저 아이디
}
