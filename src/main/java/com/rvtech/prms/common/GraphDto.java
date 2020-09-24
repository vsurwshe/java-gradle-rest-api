package com.rvtech.prms.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class GraphDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String xaxis;
	
	private int hired;
	
	private int left;
}
