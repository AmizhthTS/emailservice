package com.amizhth.email.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amizhth.email.entitymodel.AWSConfigModel;

@Repository
public interface AWSConfigDao extends JpaRepository<AWSConfigModel, Integer> {
	
	AWSConfigModel findByTenantid(int tenantid);
	
}