package com.amizhth.email.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -3694200065347169867L;

	public boolean validatePrivilege(String jwtToken) {
		boolean authorizedService = false;
		String payload = getPayload(jwtToken);
		System.out.println("payload :: " + payload);
		JSONObject jsonPayload = new JSONObject(payload);
		if (jsonPayload.has("servicePrivilleges")) {
			JSONArray arrJson = jsonPayload.getJSONArray("servicePrivilleges");
			int i;
			for (i = 0; i < arrJson.length(); i++) {
				if (arrJson.getString(i).equalsIgnoreCase("emailservice")) {
					authorizedService = true;
				}
			}
		}
		return authorizedService;
	}

	public Boolean validateToken(String jwtToken) {
		boolean validToken = false;
		String payload = getPayload(jwtToken);
		JSONObject jsonPayload = new JSONObject(payload);
		if (jsonPayload.has("exp")) {
			LocalDateTime currentTime =LocalDateTime.now(ZoneOffset.UTC);
			System.out.println("currentTime :: "+currentTime);
			
			LocalDateTime epochStartTime = LocalDateTime.parse("1970-01-01T00:00:00");
			System.out.println("epochStartTime :: "+epochStartTime);
			LocalDateTime tokenExpirationTime = epochStartTime.plusSeconds(jsonPayload.getLong("exp"));
			System.out.println("tokenExpirationTime :: "+tokenExpirationTime.atOffset(ZoneOffset.UTC));
			if(currentTime.isBefore(tokenExpirationTime)) {
				validToken = true;
			}
		}
		return validToken;
	}

	private String getPayload(String token) throws RuntimeException {
		String[] chunks = token.split("\\.");
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(chunks[1]));
		return payload;
	}

}