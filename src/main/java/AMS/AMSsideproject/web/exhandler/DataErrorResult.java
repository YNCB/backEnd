package AMS.AMSsideproject.web.exhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataErrorResult<T> extends BaseErrorResult{

    //@ApiModelProperty(dataType = "[AMSsideproject.web.exhandler.advice.UserValidExceptionDto;")
    private T data;

    public DataErrorResult(String message, String code, String status, T data) {
        super(message, code, status);
        this.data = data;
    }

}
