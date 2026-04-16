package com.student.manage.interceptor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@ConditionalOnProperty(name = "interceptor.admin.enabled",havingValue ="true")
public class LoggingInterceptor implements HandlerInterceptor {

	@Value("${Admin.userId}")
	private String adminId;

	@Value("${Admin.password}")
	private String adminPass;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		System.out.println("====Intercept Requests====");
		System.out.println(this.adminId + "\n" + this.adminPass);

		String id = request.getParameter("id");
		String pass = request.getParameter("password");
		
		HttpSession session=request.getSession();
		
		if (id != null && pass != null) {
            if (adminId.equals(id) && adminPass.equals(pass)) {
                session.setAttribute("adminId", id);
                session.setAttribute("password", pass);
                System.out.println("Admin Logged In...");
                return true;
            }
        }
		
		Object sessionId = session.getAttribute("adminId");
		Object sessionPass = session.getAttribute("password");
		
		if(sessionId!=null && sessionPass!=null) {
			if(this.adminId.equals(sessionId) && this.adminPass.equals(sessionPass))
				return true;
		}
		
		//UNAUTHORIZED
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		try {
			response.getWriter().write("Unauthorized Access");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}

		return false;
	}
}
