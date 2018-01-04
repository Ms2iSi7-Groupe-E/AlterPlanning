package fr.nantes.eni.alterplanning.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
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
                .apiInfo(apiInfo())
                .produces(Collections.singleton(MediaType.APPLICATION_JSON_VALUE))
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    @Bean
    public UiConfiguration uiConfig() {
        final String DOC_EXPANSION = "list"; //list, none, full

        return new UiConfiguration(
                null, DOC_EXPANSION, "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, null
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AlterPlanning API")
                .description("API for managing alter-planning application")
                .version("1.0")
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey("AUTHORIZATION", tokenHeader, "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(Predicates.not(PathSelectors.regex("/api/auth")))
                        .build());
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Collections.singletonList(new SecurityReference("AUTHORIZATION", authorizationScopes));
    }
}
