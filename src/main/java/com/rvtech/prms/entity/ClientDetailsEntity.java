package com.rvtech.prms.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "clientDetail")
public class ClientDetailsEntity extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	@Column
	private String tanNum;

	@Column(name = "active", nullable = false, columnDefinition = "tinyint(1) default 1")
	private boolean active;

	@Column
	private String tanUrl;

	@Column
	private String gstNum;

	@Column
	private String gstUrl;
	
	@Column
	private String clientName;
	/*
	 * // bi-directional many-to-one association to BankDetailsEntity
	 * 
	 * @OneToMany(mappedBy = "clientDetailsEntity") private Set<BankDetailsEntity>
	 * bankDetailEntitySet;
	 * 
	 * public ClientDetailsEntity (){ bankDetailEntitySet = new
	 * LinkedHashSet<BankDetailsEntity>(0); }
	 */
	/*
	 * @Column private String bankDetailId;
	 * 
	 * @Column private String addressId;
	 * 
	 * @Column private String contactpersonId;
	 */

}
