package com.rsc.bhopal.security.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	// Configuring HttpSecurity
	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
			return http
				.csrf(csrf -> csrf.disable())
				.headers(headers -> headers
					// .xssProtection(xss -> xss.disable())	// NEVER do this. Disabling XSS protection should never be in a real application.
					.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
					.frameOptions(frame -> frame.sameOrigin())
					.contentTypeOptions(content -> content.disable())
					.httpStrictTransportSecurity(hsts -> hsts
						.includeSubDomains(true)
						.maxAgeInSeconds(31536000)	// 1 year in seconds
					)
					// .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self'; style-src 'self';"))
				)
				.authorizeHttpRequests(requests -> requests
					.requestMatchers("/js/**", "/css/**","/images/**", "/lib/js/**", "/lib/css/**", "/lib/fonts/**", "/font-awesome/**")
					.permitAll()
				)
				.authorizeHttpRequests(requests -> requests
					.requestMatchers("/manage/**")
					.hasRole("ADMIN")
				)
				.authorizeHttpRequests(requests -> requests
					.requestMatchers("/home/**", "/report/**")
					.hasAnyRole("ADMIN", "USER")
				)
				.authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
				.formLogin(form -> {
					form
					.loginPage("/login")
					.defaultSuccessUrl("/home")
					.failureUrl("/login?error=true")
					.permitAll();
				})
				.rememberMe(Customizer.withDefaults())
				.logout(logout -> {
					logout
					.logoutSuccessUrl("/login")
					.invalidateHttpSession(true)
					.deleteCookies("TicketingSoftwareRSCB")
					.permitAll();
				})
				.build();
	}

	// Password Encoding
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
