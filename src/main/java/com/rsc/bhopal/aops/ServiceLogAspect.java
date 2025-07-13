package com.rsc.bhopal.aops;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.annotations.RSCLog;
import com.rsc.bhopal.aops.service.ActivityLogService;
import com.rsc.bhopal.dtos.ActivityLogDTO;
import com.rsc.bhopal.dtos.LogPayload;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ServiceLogAspect {
	@Autowired
	private ActivityLogService logService;

	// @Before(value = "execution (* com.rsc.bhopal.service.*.*(..))")
	@Before("@annotation(com.rsc.bhopal.annotations.RSCLog)")
	public void beforeAdvice(JoinPoint joinPoint) throws JsonProcessingException, NoSuchMethodException, SecurityException {
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		String desc = ms.getMethod().getAnnotation(RSCLog.class).desc();
		log.debug("desc {} "+desc);
		ActivityLogDTO dto = new ActivityLogDTO();
				dto.setActionBy("admin");
				dto.setMessage(desc);
				dto.setStatus(true);
				LogPayload payload=new LogPayload();
				payload.setClassName(joinPoint.getTarget().getClass().getSimpleName());
				payload.setFunctionName(joinPoint.getSignature().getName());
				payload.setArgs(joinPoint.getArgs());
				dto.setPayload(payload);
		try {
			logService.log(dto);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
