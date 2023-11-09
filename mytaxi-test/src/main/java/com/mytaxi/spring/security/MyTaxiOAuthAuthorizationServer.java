package com.mytaxi.spring.security;

import static com.mytaxi.util.DefaultMyTaxiConstants.OAUTH_CLIENT;
import static com.mytaxi.util.DefaultMyTaxiConstants.OAUTH_GRANT_PASSWORD;
import static com.mytaxi.util.DefaultMyTaxiConstants.OAUTH_GRANT_REFRESH;
import static com.mytaxi.util.DefaultMyTaxiConstants.OAUTH_READ;
import static com.mytaxi.util.DefaultMyTaxiConstants.OAUTH_SECRET;
import static com.mytaxi.util.DefaultMyTaxiConstants.OAUTH_WRITE;
import static com.mytaxi.util.DefaultMyTaxiConstants.RESOURCE_ID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Mohan Sharma Created on 24/09/18.
 */
@Configuration
@EnableAuthorizationServer
public class MyTaxiOAuthAuthorizationServer extends AuthorizationServerConfigurerAdapter
{

	@Resource
	private UserDetailsService userDetailsService;

	@Resource
	private AuthenticationManager authenticationManager;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Value("${spring.oauth.tokenTimeout:3600}")
	private int expiration;

	@Autowired
	private TokenStore tokenStore;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer configurer) throws Exception {
		configurer.authenticationManager(authenticationManager);
		configurer.userDetailsService(userDetailsService);
		configurer.tokenStore(tokenStore);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
				.inMemory()
				.withClient(OAUTH_CLIENT)
				.secret(passwordEncoder.encode(OAUTH_SECRET))
				.accessTokenValiditySeconds(expiration)
				.scopes(OAUTH_READ, OAUTH_WRITE)
				.authorizedGrantTypes(OAUTH_GRANT_PASSWORD, OAUTH_GRANT_REFRESH)
				.resourceIds(RESOURCE_ID);
	}
}
