package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.web.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.security.filter.jwt.JwtAccessDeniedHandler;
import AMS.AMSsideproject.web.security.filter.jwt.JwtAuthenticationEntryPoint;
import AMS.AMSsideproject.web.security.filter.jwt.JwtAuthenticationFilter;
import AMS.AMSsideproject.web.security.filter.user.UserLoginFailureCustomHandler;
import AMS.AMSsideproject.web.security.filter.user.UserLoginSuccessCustomHandler;
import AMS.AMSsideproject.web.security.filter.user.UsernamePasswordAuthenticationCustomFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig config;
    private final ObjectMapper objectMapper;

    private final JwtProvider jwtProvider;

    private final UserLoginSuccessCustomHandler successHandler;
    private final UserLoginFailureCustomHandler failureHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

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
                .antMatchers(HttpMethod.POST,"/codebox/","/codebox/{nickname:^((?!setting|logout|write|login).)*$}")
                .antMatchers(HttpMethod.GET,"/codebox/*/{*[0-9]*$+}" )

                //swagger
                .antMatchers("/swagger-ui.html/**", "/swagger/**", "/v2/api-docs", "/swagger-resources/**", "/webjars/**")
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**")

                //test
                .antMatchers( "/test", "/login/oauth2/code/kakao", "/login/oauth2/code/google", "/tokenParsingTest");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf().disable()

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 시큐리티가 제공해주는 폼 로그인 UI 사용안함
                // 헤더에 토큰으로 "basic "으로 된 토큰을 사용하는 경우
                //->httpBasic() / 사용하지 않으면 "BasicAu~"가 작동안하는데 우리는 JWT 토큰을 사용하니 커스텀해서 등록해주기
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()

                // 커스텀 필터 등록
                .apply(new MyCustomDsl())
                .and()

                //인증, 권한 api 설정
                .authorizeRequests()

                 //유저 관련
                .antMatchers("/codebox/setting").hasAuthority("USER") //get, put
                .antMatchers(HttpMethod.GET, "/codebox/logout").hasAuthority("USER")

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
                .antMatchers("/codebox/follow/*").hasAuthority("USER")

                .and()
                .build();
    }

    //jwt 커스텀 필터 등록
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http)  {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http.addFilter(config.corsFilter()); //스프링 시큐리티 필터내에 cors 관련 필터가 있음!! 그래서 제공해주는 필터 객체를 생성후 HttpSecurity에 등록!
            //http.addFilterBefore(new UsernamePasswordAuthenticationCustomFilter(authenticationManager, objectMapper , successHandler, failureHandler),
            //        UsernamePasswordAuthenticationFilter.class);
            http.addFilter(new UsernamePasswordAuthenticationCustomFilter(authenticationManager, objectMapper , successHandler, failureHandler));
            http.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider));

        }
    }
}
