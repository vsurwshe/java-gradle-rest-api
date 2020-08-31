package com.rvtech.prms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ratecard")
public class RateCardEntity extends BaseEntity<String> {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	@Column(name = "active", nullable = false, columnDefinition = "tinyint(1) default 1")
	private boolean active;

	@Column
	private String domainName;
	@Column
	private Float fromYearOfExp;
	@Column
	private Float toYearOfExp;
	@Column
	private String skillCategory;

	@Column
	private String skillSet;

	@Column
	private String clientId;
	
	@Column
	private Float rate;
	
	@Column
	private String rateCardType;
	
	@Column
	private Float rateCardDuration;

}
