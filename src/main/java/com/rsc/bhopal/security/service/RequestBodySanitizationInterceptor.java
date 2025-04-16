package com.rsc.bhopal.security.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@WebFilter("/*")
@Slf4j
public class RequestBodySanitizationInterceptor extends OncePerRequestFilter {
	private HttpServletRequest httpServletRequest;
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		this.httpServletRequest = httpServletRequest;
		if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
			if ("application/x-www-form-urlencoded".equalsIgnoreCase(httpServletRequest.getContentType())) {
				sanitizeRequestParameter(httpServletRequest);
			}
			if ("application/json".equalsIgnoreCase(httpServletRequest.getContentType())) {
				sanitizeRequestBody(httpServletRequest);
			}
		}
		filterChain.doFilter(this.httpServletRequest, httpServletResponse);
	}

	private void sanitizeRequestParameter(HttpServletRequest httpServletRequest) {
		httpServletRequest.getParameterMap().forEach((key, values) -> {
			// log.debug("Filtering out input parameter: {}", key);
			for (int i = 0; i < values.length; ++i) {
				log.debug("Filtering: Key: {}, Value: {}", key, values[i]);
				values[i] = sanitize(values[i]);
			}
		});
	}

	private void sanitizeRequestBody(HttpServletRequest httpServletRequest) throws IOException {
		StringWriter stringWriter = new StringWriter();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringWriter.write(line);
		}
		String body = stringWriter.toString();
		String sanitizedBody = sanitize(body);
		HttpServletRequest sanitizedRequest = new HttpServletRequestWrapper(httpServletRequest) {
			@Override
			public BufferedReader getReader() throws IOException {
				return new BufferedReader(new StringReader(sanitizedBody));
			}
		};
		this.httpServletRequest = sanitizedRequest;
	}

	private String sanitize(String input) {
		return input
			.replaceAll("<", "&lt;")
			.replaceAll(">", "&gt;")
			.replaceAll("&", "&amp;")
			.replaceAll("'", "&apos;")
			.replaceAll("\"", "&quot;");
	}
}
