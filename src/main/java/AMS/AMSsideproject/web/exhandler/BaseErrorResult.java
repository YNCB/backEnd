package AMS.AMSsideproject.web.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseErrorResult {

    private String message;
    private String code;
    private String status;
}
