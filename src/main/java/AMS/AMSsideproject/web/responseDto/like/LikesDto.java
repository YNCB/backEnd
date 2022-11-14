package AMS.AMSsideproject.web.responseDto.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikesDto {

    private Long userId;
    private String nickname;

}
