package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class LogPayload {
	private String className;
	private String functionName;
	private Object[]  args;
}
