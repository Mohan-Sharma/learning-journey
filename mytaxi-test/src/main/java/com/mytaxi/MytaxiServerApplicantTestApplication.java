package com.mytaxi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mytaxi.util.DefaultMyTaxiConstants;
import com.mytaxi.util.LoggingInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class MytaxiServerApplicantTestApplication extends WebMvcConfigurerAdapter
{

    public static void main(String[] args)
    {
        SpringApplication.run(MytaxiServerApplicantTestApplication.class, args);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
    }


    @Bean
    public Docket docket()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()))
                .apiInfo(generateApiInfo());
    }


    private ApiInfo generateApiInfo()
    {
        return new ApiInfo("mytaxi Server Applicant Test Service", "This service is to check the technology knowledge of a server applicant for mytaxi.", "Version 1.0 - mw",
                "urn:tos", "career@mytaxi.com", "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("http://localhost:8080/oauth/token");
        SecurityScheme oauth = new OAuthBuilder().name("oauth2")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations")
        };
        return scopes;
    }

    private SecurityContext securityContext()
    {
        return SecurityContext
                .builder()
                .securityReferences(Collections.singletonList(new SecurityReference("oauth2", scopes())))
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return  SecurityConfigurationBuilder
                .builder()
                .clientId(DefaultMyTaxiConstants.OAUTH_CLIENT)
                .clientSecret(DefaultMyTaxiConstants.OAUTH_SECRET)
                .appName("My Taxi APIS")
                .scopeSeparator(",")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }
}
