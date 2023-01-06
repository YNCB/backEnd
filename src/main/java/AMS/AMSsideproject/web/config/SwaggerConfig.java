package AMS.AMSsideproject.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 설정
 */
@Configuration
@EnableSwagger2
@EnableWebMvc  //spring-security와 연결할 때 이 부분이 없으면 404에러가 뜬다.
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("CODEBOX")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("AMS.AMSsideproject.web.apiController")) //해당 패키지 아래 controller 대상으로 api 문서 생성
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false); //기본적으로 제공해주는 response 응답 사용안함.
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CODEBOX side-project API 서버문서")
                .description("API 서버 문서 입니다.")
                .version("2.9.2")
                .build();
    }

    /**
     * Swagger2 버전은 http://localhost:8080/swagger-ui.html
     * spring-security와 연결할 때 이 부분을 작성하지 않으면 404에러가 뜬다.
     *
     * 3.x 버전 부터는 swagger-ui 경로가 다르다고 합니다.
     * http://localhost:8080/swagger-ui/index.html 로 접근해보세요
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        // -- Static resources
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }

}
