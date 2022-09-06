package AMS.AMSsideproject.web.config;

import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.security.filter.UserLoginFailureCustomHandler;
import AMS.AMSsideproject.web.custom.security.filter.UserLoginSuccessCustomHandler;
import AMS.AMSsideproject.web.custom.security.filter.UsernamePasswordCustomFilter;
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
    private final ObjectMapper objectMapper;
    private final UserLoginSuccessCustomHandler successHandler;
    private final UserLoginFailureCustomHandler failureHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/ams/join/token/kakao","/ams/join", "/ams/login/kakao")
                .antMatchers("/ams/token/refresh")

                .antMatchers("/test", "/login/oauth2/code/kakao"); //test용
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .apply(new MyCustomDsl())
                .and()

                //.authorizeRequests()
                //.antMatchers("/api/page/**").hasAuthority("USER")
                //.anyRequest().permitAll()

                //.and()
                .build();

    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http)  {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http.addFilter(config.corsFilter());
            http.addFilter(new UsernamePasswordCustomFilter(authenticationManager, objectMapper , successHandler, failureHandler));

        }
    }
}
