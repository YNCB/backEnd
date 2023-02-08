package AMS.AMSsideproject.web.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {

    private String Authorization; //엑세스 토큰 헤더 key
    private String RefreshToken; //리프레쉬 토큰 헤더 key

}
