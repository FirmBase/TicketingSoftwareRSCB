package com.rsc.bhopal.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class LogDTO {
	private Long id;
	private Date actionAt;
	private Long actionBy;
	private String message;
	
	private String payload;
	
	private Boolean status;
	public LogDTO() {
	}
	public LogDTO(final Long id, final Date actionAt, final Long actionBy, final String message, final String payload, final Boolean status) {
		this.id = id;
		this.actionAt = actionAt;
		this.actionBy = actionBy;
		this.message = message;
		this.payload = payload;
		this.status = status;
	}
}
