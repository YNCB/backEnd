package AMS.AMSsideproject.web.oauth.provider.profile;

import lombok.Data;

@Data
public class KakaoProfile {

    public String id; //User의 userid에 들어가기위해서 String으로 선언
    public String connected_at;
    public KakaoAccount kakao_account;

    @Data
    public class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Boolean profile_image_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        public Boolean has_birthday;
        public Boolean birthday_needs_agreement;
        public String birthday;
        public String birthday_type;


        @Data
        public class Profile {
            public String nickname;
        }
    }

}
