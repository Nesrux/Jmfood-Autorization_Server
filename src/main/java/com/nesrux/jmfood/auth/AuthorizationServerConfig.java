package com.nesrux.jmfood.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder pass;
	@Autowired
	private	AuthenticationManager menager;
	@Autowired
	private UserDetailsService detailsService;
	
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory()
			.withClient("jmfoodWeb")
				.secret(pass.encode("web123"))
				.authorizedGrantTypes("password", "refresh_token")
				.scopes("write", "read")
				.accessTokenValiditySeconds(60 * 60 * 6) // 6 horas
				.refreshTokenValiditySeconds(60 * 60 * 24 * 7) //7 dias
			.and()
				.withClient("checkToken")
				.secret(pass.encode("token123"))
			.and()
				.withClient("jmfood-faturamento")
				.secret(pass.encode("faturamento123"))
				.authorizedGrantTypes("client_credentials")
				.scopes("read")
			.and()
				.withClient("foodanalytics")
				.secret(pass.encode("food123"))
				.authorizedGrantTypes("authorization_code")
				.redirectUris("http://aplicacao-cliente")
				.scopes("read", "write");
			
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(menager)
			.userDetailsService(detailsService)
			.reuseRefreshTokens(false) //Ele invalida o refresh token antigo	
			.accessTokenConverter(jwtAccessTokenConverter());
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
	//	security.checkTokenAccess("isAuthenticated()"); precisa do login e senha do user
		security.checkTokenAccess("permitAll()");//Não precisa de login e senha, qualquer um consegue ver a autenticação
	}
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter(){
		JwtAccessTokenConverter jwt =  new JwtAccessTokenConverter();
		//jwt.setSigningKey("jkaie3049kanc8alpeo029irydhznalep029alsk18s"); chave simétrica
		
		var JksResource = new ClassPathResource("keystore/jmfood.jks");
		var keyStorePass = "marcos10"; //Senha do jks
		var KeyPairAlias = "jmfood";
		
		var keyStoreKeyFactory = new KeyStoreKeyFactory(JksResource, keyStorePass.toCharArray());
		var keyPair =  keyStoreKeyFactory.getKeyPair(KeyPairAlias);
		
		jwt.setKeyPair(keyPair);

		return jwt;
	}

}
