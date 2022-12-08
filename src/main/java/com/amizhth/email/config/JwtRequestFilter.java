package com.amizhth.email.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.amizhth.email.util.JwtTokenUtil;
import com.amizhth.email.multitenancy.TenantContext;
import java.util.Base64;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		String token = request.getHeader("token");
		String[] chunks = token.split("\\.");
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(chunks[1]));
		System.out.println("payload :: "+payload);
		
		JSONObject jsonPayload = new JSONObject(payload);
		if(jsonPayload.has("servicePrivilleges")) {
			JSONArray arrJson = jsonPayload.getJSONArray("servicePrivilleges");
			int i;
			boolean authorizedService = false;
			for( i = 0; i < arrJson.length(); i++) {
				System.out.println("json string :: "+arrJson.getString(i));
				if(arrJson.getString(i).equalsIgnoreCase("emailservice")) {
					authorizedService = true;
				}
			}
			System.out.println("authorizedService :: "+authorizedService);
			if(authorizedService) {
				chain.doFilter(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have Authorization to use this Service");
			}
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not Authorzied to use this Service");
		}
	}

}
