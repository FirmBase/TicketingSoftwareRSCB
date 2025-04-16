package com.rsc.bhopal.entity;

import java.util.Date;
import java.util.List;

import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.enums.VisitorsCategoryEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "RSC_TS_VISITOR_TYPE_MASTER")
public class VisitorsType {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 

	@Column(name = "VISITOR_NAME")
	private String name; 
	
	@Column(name = "CATEGORY")
	@Enumerated(EnumType.STRING)
	private VisitorsCategoryEnum category; 

	@Column(name = "GROUP_TYPE")
	@Enumerated(EnumType.STRING)
	private GroupType groupType; 

	@Column(name = "MIN_MEMBERS")
	private Integer minMembers; 

	@Column(name = "FIXED_MEMBERS")
    private Integer fixedMembers;

	@Column(name = "ADDED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addedAt;

	@Column(name = "IS_ACTIVE")
    private Boolean isActive;

	@Column(name = "IS_DEFAULT")
    private Boolean isDefault;

	@ManyToOne
	@JoinColumn(name = "ADDED_BY", referencedColumnName = "ID")
    private RSCUser addedBy;

	@OneToMany(mappedBy = "visitorsType")
	@Transient
	private List<TicketsRatesMaster> rate;

	public VisitorsType(String name,VisitorsCategoryEnum category,int minMembers,int fixedMembers ){
		this.name=name;
		this.category=category;
		this.minMembers=minMembers;
		this.fixedMembers=fixedMembers;	 
	}
}
