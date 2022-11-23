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
		System.out.println("In preHandle we are Intercepting the Request");
		System.out.println("request :: "+request.toString());
		/*System.out.println("____________________________________________");
		String requestURI = request.getRequestURI();
		String tenantID = request.getHeader("X-TenantID");
		System.out.println("RequestURI::" + requestURI + " || Search for X-TenantID  :: " + tenantID);
		
		Enumeration<String> nameString= request.getHeaderNames();
		while(nameString.hasMoreElements()) {
			String headername  = nameString.nextElement();
			System.out.println("header -> "+headername+"   value -> "+request.getHeader(headername));
		}
		System.out.println();
		System.out.println("____________________________________________");
		if (tenantID == null) {
			if(request.getHeader("sec-fetch-mode").equalsIgnoreCase("cors")) {
				System.out.println("inside cors");
				response.setStatus(200);
				return true;
			} else {
				response.getWriter().write("X-TenantID not present in the Request Header");
				response.setStatus(400);
				return false;			
			}
		}
		TenantContext.setCurrentTenant(tenantID);*/
		return true;
	}  

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.clear();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");


	}

}
