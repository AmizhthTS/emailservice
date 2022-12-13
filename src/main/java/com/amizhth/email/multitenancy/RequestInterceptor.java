package com.amizhth.email.multitenancy;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

		System.out.println("In preHandle RequestInterceptor :: response Status :: " + response.getStatus());
		if(response.getStatus() >= 200 && response.getStatus() < 300) {
			return true;
		} else if (response.getStatus() == 400) {
			response.setContentType("application/json");
			response.setStatus(400);
			response.getWriter().write(
					"{\"error\":{\"errorCode\":\"400\",\"errorMessage\":\"Bad Request\"}}");
			return false;
		} else if (response.getStatus() == 401) {
			response.setContentType("application/json");
			response.setStatus(401);
			response.getWriter().write("{\"error\":{\"errorCode\":\"401\",\"errorMessage\":\"Invalid Token\"}}");
			return false;
		} else if (response.getStatus() == 403) {
			response.setContentType("application/json");
			response.setStatus(403);
			response.getWriter().write(
					"{\"error\":{\"errorCode\":\"403\",\"errorMessage\":\"You do not have authroization to use this service\"}}");
			return false;
		} else {
			response.setContentType("application/json");
			response.getWriter().write(
					"{\"error\":{\"errorCode\":\""+response.getStatus()+"\",\"errorMessage\":\"Unable to process your request\"}}");
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.clear();
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "*");

	}

}
