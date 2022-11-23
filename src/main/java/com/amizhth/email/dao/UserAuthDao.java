package com.amizhth.email.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amizhth.email.entitymodel.AuthModel;

@Repository
public interface UserAuthDao extends JpaRepository<AuthModel, Integer> {

	AuthModel findByMobilenumberEqualsAndPasswordEqualsAndStatusEquals(@Param("mobilenumber") String mobilenumber,
			@Param("password") String password, @Param("status") Integer status);

	AuthModel findByMobilenumberEqualsAndStatusEqualsAndTenantid(@Param("mobilenumber") String mobilenumber,
			@Param("status") Integer status,@Param("tenantid") int tenantid );

	AuthModel findByMobilenumberEqualsAndStatusEquals(@Param("mobilenumber") String mobilenumber,
			@Param("status") Integer status );
}