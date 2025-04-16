package com.rsc.bhopal.entity;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.Cascade;

import org.hibernate.annotations.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "RSC_TS_PACKAGE_DETAILS")
public class PackageDetails {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "RECEIPT_NO")
	private String receiptNo;

	@Column(name = "BILL_DATE")
	@Temporal(TemporalType.DATE)
	private Date billDate;

	@Column(name = "PRICE")
	private Float price;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne
	@JoinColumn(name = "ADDED_BY", referencedColumnName = "ID")
	private RSCUser addedBy;

	@ManyToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	@JoinTable(name = "RSC_TS_PACKAGE_GROUPS",
		joinColumns = {@JoinColumn(name = "package_id")},
		inverseJoinColumns = {@JoinColumn(name = "visitor_id")})
	private Set<VisitorsType> visitorsTypes;
}
