package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.interceptor.RefreshTokenInterceptor;
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

    /**
     * 이렇게 주입이 되나?!!!!!!!!!!!!!!!!!!!!!
     */
    private final ObjectMapper objectMapper;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() { return new BCryptPasswordEncoder();}

    /** 인터셉터 등록 **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(jwtProvider, refreshTokenService , objectMapper))
                .order(1)
                .addPathPatterns("/codebox/refreshToken")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!
    }
}
