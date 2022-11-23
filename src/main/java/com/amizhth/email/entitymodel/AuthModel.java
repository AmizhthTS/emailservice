package com.amizhth.email.entitymodel;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "auth")
public class AuthModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String mobilenumber;
	private String email;
	private Integer otp;
	private Long validity;
	private Integer status;
	private String password;
	private int tenantid;
	private String firstname;
	private String role;
	private String lastname;
	private int entityid;
	private String createdby;
	private LocalDateTime createdtime;
	private String modifiedby;
	private LocalDateTime modifiedtime;

}
