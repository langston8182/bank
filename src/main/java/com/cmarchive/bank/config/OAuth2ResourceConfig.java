package com.cmarchive.bank.config;

import com.cmarchive.bank.component.MyAuthenticationSuccessHandler;
import com.cmarchive.bank.component.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableOAuth2Sso
public class OAuth2ResourceConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private MyAuthenticationSuccessHandler authenticationHandler;

    @Autowired
    private MyLogoutSuccessHandler logoutHandler;

    /*@Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:8090/oauth/check_token");
        remoteTokenServices.setClientId("client");
        remoteTokenServices.setClientSecret("password");

        return remoteTokenServices;
    }*/

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login/**")
                .permitAll()
                .antMatchers("/users/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated().and()
                .csrf().disable();
    }
}
