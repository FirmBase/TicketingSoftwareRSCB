package com.rsc.bhopal.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessage<T> {
	private boolean status;
	private String message;
	private T data;
}
