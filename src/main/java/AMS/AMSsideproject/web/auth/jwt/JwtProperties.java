package AMS.AMSsideproject.web.auth.jwt;

/**
 * jwt 토큰과 관련된 속성들 모음
 */
public interface JwtProperties {

    public String SECRET = "AMS-sideProject";
    public int ACCESSTOKEN_TIME = 3000; // (1/1000초)
    public int REFRESHTOKEN_TIME = 300000;

}
