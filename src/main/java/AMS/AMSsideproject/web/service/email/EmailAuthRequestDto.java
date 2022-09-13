package AMS.AMSsideproject.web.service.email;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EmailAuthRequestDto {

    public String email;
}
