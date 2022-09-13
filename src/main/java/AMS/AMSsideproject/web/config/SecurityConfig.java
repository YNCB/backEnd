package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.security.filter.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorConfig config;
    private final JwtProvider jwtProvider;

    private final UserService userService;

    private final ObjectMapper objectMapper;
    private final UserLoginSuccessCustomHandler successHandler;
    private final UserLoginFailureCustomHandler failureHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/codebox/login/token/kakao", "/codebox/login/token/google")
                .antMatchers("/codebox/join", "/codebox/join/mailConfirm", "/codebox/join/validNickName")
                .antMatchers("/codebox/token/refresh")

                .antMatchers( "/test", "/login/oauth2/code/kakao", "/login/oauth2/code/google"); //test용
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable() //헤더에 토큰으로 "basic "으로 된 토큰을 사용하는 경우 -> httpBasic() / 사용하지 않으면 "BasicAu~"가 작동안하는데 우리는 JWT 토큰을 사용하니 커스텀해서 등록해주기

                .apply(new MyCustomDsl())

                //.and()
                //.authorizeRequests()
                //.antMatchers("/test").authenticated()

                //.antMatchers("/test").authenticated()
                //.anyRequest().permitAll()

                .and()
                .build();

    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http)  {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http.addFilter(config.corsFilter());
            http.addFilter(new UsernamePasswordAuthenticationCustomFilter(authenticationManager, objectMapper , successHandler, failureHandler));
            http.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider, objectMapper, userService));

        }
    }
}
