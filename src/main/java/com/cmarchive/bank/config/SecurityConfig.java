package com.cmarchive.bank.config;

import com.cmarchive.bank.component.MyAuthenticationSuccessHandler;
import com.cmarchive.bank.component.MyLogoutSuccessHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailService;
	private MyAuthenticationSuccessHandler authenticationHandler;
	private MyLogoutSuccessHandler logoutHandler;

    @Value("${security.allow.origin}")
    private String securityAllowedHosts;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	@Autowired
	public SecurityConfig(UserDetailsService userDetailService, MyAuthenticationSuccessHandler authenticationHandler, MyLogoutSuccessHandler logoutHandler) {
		super();
		this.userDetailService = userDetailService;
		this.authenticationHandler = authenticationHandler;
		this.logoutHandler = logoutHandler;
	}

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
		auth.authenticationProvider(authProvider());
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		http
            .cors()
            .and()
			.authorizeRequests()
				.antMatchers("/users/**").hasRole("ADMIN")
				.antMatchers("/").permitAll()
				.anyRequest().hasRole("USER")
                .anyRequest().fullyAuthenticated()
				.and()
                .requestCache()
                .requestCache(new NullRequestCache())
                .and()
				.httpBasic()
				.and()
                .addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class)
                .csrf().disable()
			.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.successHandler(authenticationHandler)
				.failureUrl("/login?error=true")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessHandler(logoutHandler)
				.permitAll();
	}

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(ObjectUtils.defaultIfNull(securityAllowedHosts, CorsConfiguration.ALL));
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
	
}
