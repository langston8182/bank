package com.cmarchive.bank.config;

import com.cmarchive.bank.component.MyAuthenticationSuccessHandler;
import com.cmarchive.bank.component.MyLogoutSuccessHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailService;
    private MyAuthenticationSuccessHandler authenticationHandler;
    private MyLogoutSuccessHandler logoutHandler;
    private AuthenticationTokenFilter authenticationTokenFilter;

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
    public SecurityConfig(UserDetailsService userDetailService,
                          MyAuthenticationSuccessHandler authenticationHandler,
                          MyLogoutSuccessHandler logoutHandler,
                          AuthenticationTokenFilter authenticationTokenFilter) {
        super();
        this.userDetailService = userDetailService;
        this.authenticationHandler = authenticationHandler;
        this.logoutHandler = logoutHandler;
        this.authenticationTokenFilter = authenticationTokenFilter;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
        auth.authenticationProvider(authProvider());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authProvider())
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/users/**").hasRole("ADMIN")
                .antMatchers("/").permitAll()
                .anyRequest().hasRole("USER")

                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authenticationHandler)
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutHandler)

                .and()
                .addFilterBefore(new AuthenticationTokenFilter(super.authenticationManager()), BasicAuthenticationFilter.class)
                .exceptionHandling();
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