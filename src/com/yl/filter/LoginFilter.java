package com.yl.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yl.common.user.pojo.UserView;
import com.yl.common.util.ConfigUtil;

public class LoginFilter implements Filter {

	public static final String logout_page = "/sms-dms/login.jsp";
	public static final String phone_logout_page = "/sms-dms/transaction/phoneApp/login.jsp";

	public void destroy() {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestUrl = request.getRequestURI();
		
		HttpSession session =  request.getSession();
		UserView user = (UserView) session.getAttribute("userInfo");
		
		
		if(requestUrl.contains("getwayInterface")){
			filterChain.doFilter(request, response);
			return;
		}else if(!(requestUrl.contains("login")) && user == null) {
			response.sendRedirect(logout_page);
			return;
		} else {
			filterChain.doFilter(request, response);
			return;
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public static void main(String[] args) {
		System.out.println("ss".equals(new String("ss")));
	}
}
