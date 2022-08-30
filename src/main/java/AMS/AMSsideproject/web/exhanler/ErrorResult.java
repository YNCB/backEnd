package AMS.AMSsideproject.web.exhanler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult {

    private String message;
    private String code;
    private String status;
}
