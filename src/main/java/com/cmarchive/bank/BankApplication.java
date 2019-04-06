package com.cmarchive.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
public class BankApplication {

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}
}
