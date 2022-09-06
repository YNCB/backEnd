package AMS.AMSsideproject.web.oauth.provider.token;

import lombok.Data;

@Data
public class GoogleToken {

    private String access_token;
    private String token_type;
    //private String refresh_token;
    private Integer expires_in;
    private String scope;
    private String id_token;

}
