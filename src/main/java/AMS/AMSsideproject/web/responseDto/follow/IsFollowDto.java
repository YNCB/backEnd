package AMS.AMSsideproject.web.responseDto.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsFollowDto {

    private Long followId;
    private Boolean isFollow;
}
