package AMS.AMSsideproject.web.auth.jwt;

/**
 * jwt 토큰과 관련된 속성들 모음
 */
public interface JwtProperties {

    public String SECRET = "CODEBOX-sideProject";
    public int ACCESSTOKEN_TIME = 300000; // (1/1000초)
    public int REFRESHTOKEN_TIME = 3000000;
    public String HEADER_STRING = "accessToken";
    public String REFRESH_HEADER_STRING = "refreshToken";



}
