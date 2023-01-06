package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.security.filter.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig config;
    private final JwtProvider jwtProvider;

    private final UserService userService;

    private final ObjectMapper objectMapper;
    private final UserLoginSuccessCustomHandler successHandler;
    private final UserLoginFailureCustomHandler failureHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                //유저관련(소셜로그인)
                .antMatchers("/codebox/login/token/kakao", "/codebox/login/token/google")

                //유저관련(회원가입)
                .antMatchers("/codebox/join*", "/codebox/join/mailConfirm", "/codebox/join/validNickName")

                //리프레쉬 토큰 관련
                .antMatchers("/codebox/refreshToken")

                //게시물 관련(정규식 표현)
                .antMatchers(HttpMethod.GET,"/codebox/","/codebox/{nickname:^((?!setting).)*$}","/codebox/*/{*[0-9]*$+}")

                //swagger
                .antMatchers("/swagger-ui.html/**", "/swagger/**", "/v2/api-docs", "/swagger-resources/**", "/webjars/**")
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**")

                //test
                .antMatchers( "/test", "/login/oauth2/code/kakao", "/login/oauth2/code/google");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable() //헤더에 토큰으로 "basic "으로 된 토큰을 사용하는 경우 -> httpBasic() / 사용하지 않으면 "BasicAu~"가 작동안하는데 우리는 JWT 토큰을 사용하니 커스텀해서 등록해주기

                .apply(new MyCustomDsl())
                .and()

                .authorizeRequests()
                 //유저 관련
                .antMatchers("/codebox/setting").hasAuthority("USER") //get, put

                //게시물 관련
                .antMatchers(HttpMethod.POST, "/codebox/write").hasAuthority("USER")
                .antMatchers("/codebox/*/*/edit").hasAuthority("USER") //get, post
                .antMatchers(HttpMethod.DELETE, "/codebox/*/*").hasAuthority("USER")

                //댓글 관련
                .antMatchers(HttpMethod.POST, "/codebox/*/*/reply/add").hasAuthority("USER")
                .antMatchers("/codebox/*/*/reply/*").hasAuthority("USER") //get,put,delete

                //좋아요 관련
                .antMatchers( "/codebox/*/*/like").hasAuthority("USER") //post,get

                //팔로우 관련
                .antMatchers(HttpMethod.POST,"/codebox/follow/add").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/codebox/follow/*").hasAuthority("USER")
                .antMatchers(HttpMethod.GET,"/codebox/follow/follower","/codebox/follow/following").hasAuthority("USER")

                .and()
                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http)  {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http.addFilter(config.corsFilter() ); //스프링 시큐리티 필터내에 cors 관련 필터가 있음!! 그래서 제공해주는 필터 객체를 생성후 HttpSecurity에 등록!
            http.addFilter(new UsernamePasswordAuthenticationCustomFilter(authenticationManager, objectMapper , successHandler, failureHandler));
            http.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider, objectMapper, userService));

        }
    }
}
