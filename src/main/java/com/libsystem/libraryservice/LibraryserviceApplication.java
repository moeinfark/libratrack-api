package com.libsystem.libraryservice;

import com.libsystem.libraryservice.config.RateLimitingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LibraryserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryserviceApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
		FilterRegistrationBean<RateLimitingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new RateLimitingFilter());
		registration.addUrlPatterns("/api/*");
		return registration;
	}

}
