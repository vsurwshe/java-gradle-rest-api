package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DBClientDataDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Map<String, String>> clientId;
	private List<Map<String, Float>> clientData;
}
