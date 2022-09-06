package AMS.AMSsideproject.web.oauth.provider.profile;

import lombok.Data;

/**
 * 구글에는 닉네임이 존재하지 않음. 이름만 존재.
 */
@Data
public class GoogleProfile {

    public String id; //고유 아이디
    public String email; //이메일
    public boolean verified_email;
    public String picture;

}
