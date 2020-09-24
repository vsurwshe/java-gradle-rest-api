package com.rvtech.prms.common;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2730032391079553372L;

	private Long id;

	private String fileName;

	private String contentType;

	private byte[] content;

	private String description;

	private Date created;

	private boolean uploaded;

	private Long fileSize;

	private String accountId;
	
	private Date fromAttedDate;
	
	private Date toAttedDate;
}
