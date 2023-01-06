package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.web.interceptor.PostAuthInterceptor;
import AMS.AMSsideproject.web.interceptor.UserAuthenInterceptor;
import AMS.AMSsideproject.web.interceptor.RefreshTokenAuthInterceptor;
import AMS.AMSsideproject.web.interceptor.UserAuthorInterceptor;
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

        //게시물 권한
        //모든 uri를 지정하여도 인터셉터는 들어가지만 권한검사를 하지 않는다.
        registry.addInterceptor(postAuthorizationInterceptor())
                .order(1)
                .addPathPatterns("/**")
                //.addPathPatterns("/codebox/*/*/edit")
                //.addPathPatterns("/codebox/*/*") //delete 에 대해서
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

        //인증
        //모든 uri를 지정하여도 인터셉터는 들어가지만 인증검사를 하지 않는다.
        registry.addInterceptor(LoginAuthInterceptor())
                .order(1)
                .addPathPatterns("/**")
                //.addPathPatterns("/codebox/", "/codebox/*", "/codebox/*/{postId:[\\d+]}")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

        //권한
        //모든 uri를 지정하여도 인터셉터는 들어가지만 권한검사를 하지 않는다.
        registry.addInterceptor(userAuthorInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); //오류 페이지 경로 제외!!

    }

    @Bean
    public RefreshTokenAuthInterceptor refreshTokenInterceptor() {return new RefreshTokenAuthInterceptor();}
    @Bean
    public PostAuthInterceptor postAuthorizationInterceptor() {return new PostAuthInterceptor(); }
    @Bean
    public UserAuthenInterceptor LoginAuthInterceptor() {return new UserAuthenInterceptor();}
    @Bean
    public UserAuthorInterceptor userAuthorInterceptor() {return new UserAuthorInterceptor();}


    /** DefaultMessageCodesResolver 구현체 수정 **/
//    @Override
//    public MessageCodesResolver getMessageCodesResolver() {
//        DefaultMessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
//        codesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
//        return codesResolver;
//    }


}
