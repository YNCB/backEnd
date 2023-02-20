package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.domain.refreshToken.service.RefreshTokenService;
import AMS.AMSsideproject.web.interceptor.*;
import AMS.AMSsideproject.web.jwt.service.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() { return new BCryptPasswordEncoder();}

    /** 인터셉터 등록 **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //리프레시토큰 인증검증 필터
        registry.addInterceptor(refreshTokenInterceptor())
                .order(1)
                .addPathPatterns("/codebox/refreshToken")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

        //게시물 권한
        //모든 uri를 지정하여도 인터셉터는 들어가지만 권한검사를 하지 않는다.
        registry.addInterceptor(postAuthorizationInterceptor())
                .order(1)
                .addPathPatterns("/**")
                //.addPathPatterns("/codebox/*/*/edit")
                //.addPathPatterns("/codebox/*/*") //delete 에 대해서
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

        //인증,권한 검사
        registry.addInterceptor(authInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

    }
    @Bean
    public AuthInterceptor authInterceptor() {return new AuthInterceptor();}

    @Bean
    public PostAuthInterceptor postAuthorizationInterceptor() {return new PostAuthInterceptor(); }

    @Bean
    public RefreshTokenAuthInterceptor refreshTokenInterceptor() {
        return new RefreshTokenAuthInterceptor(jwtProvider, refreshTokenService);}

}
