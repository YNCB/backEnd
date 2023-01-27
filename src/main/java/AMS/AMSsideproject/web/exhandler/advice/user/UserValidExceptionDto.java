package AMS.AMSsideproject.web.exhandler.advice.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserValidExceptionDto {

    @ApiModelProperty(example = "필드 이름")
    private String key;
    @ApiModelProperty(example = "사용자가 입력한 필드 값")
    private Object value;
    @ApiModelProperty(example = "필드 조건 메시지")
    private String message;
}
