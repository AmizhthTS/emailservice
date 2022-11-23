package com.amizhth.email.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amizhth.email.util.UtilConstants;

@Configuration
@EnableConfigurationProperties({ MultiTenantProperties.class, JpaProperties.class })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.amizhth.email.dao", entityManagerFactoryRef = "multiEntityManager", 
						transactionManagerRef = "multiTransactionManager")
public class MultiTenantJpaConfiguration {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MultiTenantProperties multiTenantProperties;

	private final ThreadLocal<String> currentTenant = new ThreadLocal<>();
	private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
	private AbstractRoutingDataSource multiTenantDataSource;
	
	@Bean(name = "dataSourcesDvdRental")
	public DataSource dataSource() {
		multiTenantDataSource = new AbstractRoutingDataSource() {
			@Override
			protected Object determineCurrentLookupKey() {
				return currentTenant.get();
			}
		};
		multiTenantDataSource.setTargetDataSources(tenantDataSources);
		multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
		multiTenantDataSource.afterPropertiesSet();
		return multiTenantDataSource;
	}

	public void addTenant(String driverClassName, String tenantId, String url, String username, String password) throws SQLException {

		DataSource dataSource = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
				.username(username).password(password).build();

		// Check that new connection is 'live'. If not - throw exception
		try (Connection c = dataSource.getConnection()) {
			tenantDataSources.put(tenantId, dataSource);
			multiTenantDataSource.afterPropertiesSet();
		}
	}

	public void setCurrentTenant(String tenantId) {
		logger.info("tenantId :: " + tenantId);
		currentTenant.set(tenantId);
	}

	private DriverManagerDataSource defaultDataSource() {
		DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
		for (DataSourceProperties dsProperties : this.multiTenantProperties.getDataSources()) {
			defaultDataSource.setDriverClassName(dsProperties.getDriverClassName());
			defaultDataSource.setUrl(dsProperties.getUrl());
			defaultDataSource.setUsername(dsProperties.getUsername());
			defaultDataSource.setPassword(dsProperties.getPassword());
		}
		setCurrentTenant(UtilConstants.DEFAULT_DS);
		return defaultDataSource;
	}

	@Bean(name = "multiEntityManager")
	public LocalContainerEntityManagerFactoryBean multiEntityManager() {
		LocalContainerEntityManagerFactoryBean lCEMFB = new LocalContainerEntityManagerFactoryBean();
		lCEMFB.setDataSource(dataSource());
		lCEMFB.setPackagesToScan("com.amizhth.email.entitymodel");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		lCEMFB.setJpaVendorAdapter(vendorAdapter);
		lCEMFB.setPersistenceUnitName("email");
		lCEMFB.setJpaProperties(hibernateProperties());
		return lCEMFB;
	}

	private Properties hibernateProperties() {
		//To Do
		//Dynamically configure value for below properties from table
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", false);
		properties.put("hibernate.format_sql", false);
		return properties;
	}

	@Bean(name = "multiTransactionManager")
	public PlatformTransactionManager multiTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(multiEntityManager().getObject());
		return transactionManager;
	}
}