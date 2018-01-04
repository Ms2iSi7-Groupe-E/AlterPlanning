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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ughostephan on 03/01/2018.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("fr.nantes.eni.alterplanning.controller.api"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private List<SecurityScheme> securitySchemes() {
        final List<SecurityScheme> securitySchemes = new ArrayList<>();
        securitySchemes.add(new ApiKey("AUTHORIZATION", tokenHeader, "header"));
        return securitySchemes;
    }

    private List<SecurityContext> securityContexts() {
        final List<SecurityContext> securityContexts = new ArrayList<>();

        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(Predicates.not(PathSelectors.regex("/api/auth")))
                .build());

        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        final List<SecurityReference> securityReferences = new ArrayList<>();

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        securityReferences.add(new SecurityReference("AUTHORIZATION", authorizationScopes));

        return securityReferences;
    }
}
