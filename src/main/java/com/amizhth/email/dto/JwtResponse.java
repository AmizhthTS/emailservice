package com.amizhth.email.dto;

import java.io.Serializable; 
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(value=JsonInclude.Include.NON_EMPTY, content=JsonInclude.Include.NON_NULL)
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private String jwttoken;
	private String firstName; 
	private String businessName;
	private String url;
	private Integer tenantID;
	private Integer userID;
	private List<String> privileges;
	private String planType;
	private String subscriptionDaysRemaining;
	private String totalNumberOfProducts;
	private String refreshToken;
	private String businessType;
	private Integer themeId;
	private ErrorDTO error;
}