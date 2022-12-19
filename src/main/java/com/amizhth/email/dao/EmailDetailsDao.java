package com.amizhth.email.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amizhth.email.entitymodel.EmailDetailsModel;



@Repository
public interface EmailDetailsDao extends JpaRepository<EmailDetailsModel, Integer> {

	EmailDetailsModel findById(int id);
}