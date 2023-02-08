package AMS.AMSsideproject.web.auth.jwt;

/**
 * jwt 토큰과 관련된 속성들 모음
 */
public interface JwtProperties {

    public String SECRET = "YNCB CODEBOX-sideProject JWT SecretKey ddddddddddddddddddddddddddddddddddddddddddddddd";
    public int ACCESSTOKEN_TIME = 600000; // (1/1000초)
    public int REFRESHTOKEN_TIME = 300000000;
    public String ACCESS_HEADER_STRING = "Authorization";
    public String REFRESH_HEADER_STRING = "RefreshToken";
    public String ACCESS_PREPIX_STRING = "Bearer ";



}
