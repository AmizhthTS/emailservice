package com.amizhth.email.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.amizhth.email.entitymodel.DataSourceConfigModel;

public interface DatabaseConnectionDao extends JpaRepository<DataSourceConfigModel, Long> {
	List<DataSourceConfigModel> findByTenantid(String tenantid);

	DataSourceConfigModel findByTenantid(int tenantid);
}
