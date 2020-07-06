package com.rvtech.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rvtech.entity.ExpenceEntity;
import com.rvtech.entity.ProjectEntity;

public interface ProjectRepository extends CrudRepository<ProjectEntity, String> {

	Optional<ProjectEntity> findById(String id);

	
}
