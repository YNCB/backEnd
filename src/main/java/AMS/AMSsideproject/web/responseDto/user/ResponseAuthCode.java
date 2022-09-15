package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAuthCode {

    @ApiModelProperty(example = "aa1A22")
    public String authCode;
}
