package AMS.AMSsideproject.web.jwt;

import org.springframework.stereotype.Component;

/**
 * jwt 토큰과 관련된 속성들 모음
 */
public interface JwtProperties {

    public String SECRET = "YNCB CODEBOX-sideProject JWT SecretKey ddddddddddddddddddddddddddddddddddddddddddddddd";

    public long ACCESSTOKEN_TIME = 1000 * 60 * 30;           // 30분
    public long REFRESHTOKEN_TIME = 1000 * 60 * 60 * 24 * 7; //7일

    public String ACCESS_HEADER_STRING = "Authorization";
    public String REFRESH_HEADER_STRING = "RefreshToken";
    public String ACCESS_PREFIX_STRING = "Bearer ";

}
