package com.rvtech.prms.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2053437095061233291L;

	private String status;

	private String info;

}