package com.rsc.bhopal.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="RSC_TS_ACTIVITY_LOG")
public class ActivityLog {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "STATUS")
	private Boolean status;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "ACTION_AT")
	private Date actionAt;
	
	@ManyToOne
	@JoinColumn(name = "ACTION_BY",referencedColumnName = "ID")
	private RSCUser actionBy;

	@Column(name = "PAYLOAD",length = 1000)
	private String payload;
	
	//Data Sediugn 
	
}
