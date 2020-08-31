package com.rvtech.prms.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public interface GenericMapper {

	 public MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
	 
	 public MapperFacade mapper = mapperFactory.getMapperFacade();
}
