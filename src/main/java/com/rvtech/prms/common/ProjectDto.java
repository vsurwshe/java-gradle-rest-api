package com.rvtech.prms.common;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectDto {

	private String id;
	
	private String clientName;
	
	private String clientId;

	private String projectName;

	private boolean active;

	private String projectDesc;

	private Float projectCost;

	private String projectType;

	private String contractAttachmentUrl;

	private String projectManager;

	private String purchaseOrder;
	
	private Date projectStartDate;
	
	private Date projectEndDate;
	
	private String purchaseOrderId;
	
}
