package com.amizhth.email.controller;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.crypto.IllegalBlockSizeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amizhth.email.dto.JwtRequest;
import com.amizhth.email.dto.JwtResponse;
import com.amizhth.email.service.JwtUserDetailsService;
import com.amizhth.email.util.AesUtil;
import com.amizhth.email.util.JwtTokenUtil;
import com.amizhth.email.util.emailUser;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception{
		JwtResponse jwtResponse = new JwtResponse();
		String refreshToken = "";
		try {
			AesUtil aesUtil = new AesUtil(AesUtil.SECURITY_AES_KEYSIZE,AesUtil.SECURITY_AES_ITERATIONS);
			String encrpt = aesUtil.encrypt(authenticationRequest.getPassword());
			System.out.println(encrpt);
			String decrpt = aesUtil.decrypt(authenticationRequest.getPassword());
			logger.info("decrpted successfully :: "+decrpt);
			logger.info("Enteredddd Auth Controller");
			authenticate(authenticationRequest.getUsername(), decrpt);
			logger.info("After calling Local Authenticate");
			final UserDetails userDetails = jwtUserDetailsService
					.loadUserByUsername(authenticationRequest.getUsername());
			logger.info("loaded user details object"+userDetails);
			final String token = jwtTokenUtil.generateToken(userDetails);
			
			emailUser emailUser = (emailUser) userDetails;
			logger.info("loaded email user details object"+userDetails);
			logger.info("emailUser.getUserid() :: "+emailUser.getUserid());
			
			ArrayList<String> authorities = (ArrayList<String>) userDetails.getAuthorities().stream().map(s-> s.getAuthority()).collect(Collectors.toList());
			jwtResponse.setJwttoken(token);
			jwtResponse.setRefreshToken(refreshToken);
			jwtResponse.setPrivileges(authorities);
			jwtResponse.setFirstName(emailUser.getFirstname());
			jwtResponse.setTenantID(emailUser.getTenantid());
			jwtResponse.setUserID(emailUser.getUserid());
		}catch(Exception e) {
			logger.error("Exception in Authenticate :: "+e.getMessage());
		}
		return ResponseEntity.ok(jwtResponse);
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		logger.info("Enterrrreddddd local method "+ username + " and " + password) ;
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			logger.info("Enterrrreddddd local method "+ " manager's authenticate successs") ;
		} catch (DisabledException e) {
			throw new Exception("teste", e);
		} catch (BadCredentialsException e) {
			throw new Exception("test", e);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} 
		
	}
	
}
