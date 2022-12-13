package com.amizhth.email.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.amizhth.email.util.JwtTokenUtil;
import com.amizhth.email.logging.GlobalExceptionHandler;
import com.amizhth.email.multitenancy.TenantContext;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Qualifier("globalExceptionHandler")
	private GlobalExceptionHandler resolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			final String requestTokenHeader = request.getHeader("Authorization");
			String jwtToken = "";
			jwtToken = requestTokenHeader.substring(7);

			if (!jwtTokenUtil.validatePrivilege(jwtToken)) {
				System.out.println("Invalid Privilege");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}

			if (!jwtTokenUtil.validateToken(jwtToken)) {
				System.out.println("Invalid Token");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (RuntimeException e) {
			logger.error("Exception in Filter :: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		chain.doFilter(request, response);
	}
}