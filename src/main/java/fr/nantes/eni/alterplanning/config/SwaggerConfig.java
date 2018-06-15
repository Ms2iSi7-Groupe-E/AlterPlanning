package fr.nantes.eni.alterplanning.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    private static final String AUTHENTICATION_NAME = "Authorization (Bearer Token)";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("fr.nantes.eni.alterplanning.controller.api"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContexts()))
                .securitySchemes(Collections.singletonList(securitySchemes()));
    }

    private SecurityScheme securitySchemes() {
        return new ApiKey(AUTHENTICATION_NAME, tokenHeader, "header");
    }

    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(Predicates.and(
                        Predicates.not(PathSelectors.regex("/api/auth")),
                        Predicates.not(PathSelectors.ant("/api/file/**"))
                ))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(AUTHENTICATION_NAME, authorizationScopes));
    }
}