package com.rsc.bhopal.security.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InputSanitizationInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handlerObject) throws Exception {
		// Sanitize request parameters
		httpServletRequest.getParameterMap().forEach((key, values) -> {
			// log.debug("Filtering out input parameter: {}", key);
			for (int i = 0; i < values.length; ++i) {
				// log.debug("Filtering: Key: {}, Value: {}", key, values[i]);
				values[i] = sanitize(values[i]);	// Apply sanitization logic
			}
		});
		return true;
	}

	private String sanitize(String input) {
		// Sanitize input (e.g., strip HTML tags, escape special characters)
		return input
			.replaceAll("<", "&lt;")
			.replaceAll(">", "&gt;")
			.replaceAll("&", "&amp;")
			.replaceAll("'", "&apos;")
			.replaceAll("\"", "&quot;");
	}
}
