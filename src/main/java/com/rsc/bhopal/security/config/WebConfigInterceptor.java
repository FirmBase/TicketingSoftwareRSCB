package com.rsc.bhopal.security.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rsc.bhopal.security.service.InputSanitizationInterceptor;
import com.rsc.bhopal.security.service.RequestBodySanitizationInterceptor;

@Configuration
public class WebConfigInterceptor implements WebMvcConfigurer {
	private final InputSanitizationInterceptor inputSanitizationInterceptor;
	private final RequestBodySanitizationInterceptor requestBodySanitizationInterceptor;

	public WebConfigInterceptor(InputSanitizationInterceptor inputSanitizationInterceptor, RequestBodySanitizationInterceptor requestBodySanitizationInterceptor) {
		this.inputSanitizationInterceptor = inputSanitizationInterceptor;
		this.requestBodySanitizationInterceptor = requestBodySanitizationInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry) {
		interceptorRegistry.addInterceptor(inputSanitizationInterceptor)
			.addPathPatterns("/**")	// Apply to all paths
			.excludePathPatterns("/static/**");	// Optional: Exclude static files
	}

	@Bean
	public FilterRegistrationBean<RequestBodySanitizationInterceptor> sanitizationFilterRegistration() {
		FilterRegistrationBean<RequestBodySanitizationInterceptor> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(requestBodySanitizationInterceptor);
		registrationBean.addUrlPatterns("/**");
		return registrationBean;
	}
}
