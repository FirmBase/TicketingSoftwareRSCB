package com.rsc.bhopal.dtos;

import com.rsc.bhopal.enums.ApplicationConstantType;

import lombok.Data;

@Data
public class ApplicationConstantDTO {
	private Long id;

	private String data;

	private ApplicationConstantType type;
}
