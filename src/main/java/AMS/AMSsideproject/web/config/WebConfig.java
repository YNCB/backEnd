package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.web.interceptor.PostAddAuthInterceptor;
import AMS.AMSsideproject.web.interceptor.LoginAuthInterceptor;
import AMS.AMSsideproject.web.interceptor.RefreshTokenAuthInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        registry.addInterceptor(postAuthorizationInterceptor())
                .order(1)
                //.addPathPatterns("/codebox/*/write")
                .addPathPatterns("/codebox/*/*/edit")
                .addPathPatterns("/codebox/*/*") //delete 에 대해서
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

        registry.addInterceptor(LoginAuthInterceptor())
                .order(1)
                .addPathPatterns("/codebox/", "/codebox/*", "/codebox/*/{postId:[\\d+]}")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

    }

    @Bean
    public RefreshTokenAuthInterceptor refreshTokenInterceptor() {return new RefreshTokenAuthInterceptor();}
    @Bean
    public PostAddAuthInterceptor postAuthorizationInterceptor() {return new PostAddAuthInterceptor(); }
    @Bean
    public LoginAuthInterceptor LoginAuthInterceptor() {return new LoginAuthInterceptor();}


    /** DefaultMessageCodesResolver 구현체 수정 **/
//    @Override
//    public MessageCodesResolver getMessageCodesResolver() {
//        DefaultMessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
//        codesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
//        return codesResolver;
//    }


}
