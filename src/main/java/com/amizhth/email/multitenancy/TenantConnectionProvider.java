package com.amizhth.email.multitenancy;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final long serialVersionUID = 1L;

	@Autowired
	private Map<String, DataSource> dataSources;

	@Override
	protected DataSource selectAnyDataSource() {
		
		System.out.println("dataSources"+this.dataSources);
		return this.dataSources.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		
		System.out.println("tenantIdentifier"+this.dataSources);
		if(this.dataSources.get(tenantIdentifier) != null)
		    return this.dataSources.get(tenantIdentifier);
		else
			return this.dataSources.get("public");
	}
}
