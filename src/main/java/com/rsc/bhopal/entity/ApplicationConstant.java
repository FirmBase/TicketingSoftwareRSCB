package com.rsc.bhopal.entity;

import com.rsc.bhopal.enums.ApplicationConstantType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "RSC_TS_APPLICATION_CONSTANT")
public class ApplicationConstant {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "DATA")
	private String data;

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private ApplicationConstantType type;

/*
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID", referencedColumnName = "SERIAL_ID")
	private List<TicketBill> ticketBills;
*/
}
