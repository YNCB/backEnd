package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.interceptor.RefreshTokenInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() { return new BCryptPasswordEncoder();}

    /** 인터셉터 등록 **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshTokenInterceptor())
                .order(1)
                .addPathPatterns("/codebox/refreshToken")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!


    }
    @Bean
    public RefreshTokenInterceptor refreshTokenInterceptor() {
        return new RefreshTokenInterceptor();
    }

    /** DefaultMessageCodesResolver 구현체 수정 **/
//    @Override
//    public MessageCodesResolver getMessageCodesResolver() {
//        DefaultMessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
//        codesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
//        return codesResolver;
//    }


}
