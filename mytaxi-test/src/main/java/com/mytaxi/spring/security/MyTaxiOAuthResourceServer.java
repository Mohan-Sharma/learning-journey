package com.mytaxi.spring.security;

import static com.mytaxi.util.DefaultMyTaxiConstants.RESOURCE_ID;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * @author Mohan Sharma Created on 24/09/18.
 */
@Configuration
@EnableResourceServer
public class MyTaxiOAuthResourceServer extends ResourceServerConfigurerAdapter
{

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.and()
				.requestMatchers().antMatchers("/v1/**")
				.and()
				.authorizeRequests()
				.antMatchers("/v1/**").access("hasRole('ADMIN')")
				.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}
