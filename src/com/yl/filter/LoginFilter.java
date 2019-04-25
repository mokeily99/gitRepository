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
		
		//维护人员还原cookie
		if(user != null && "11201".equals(user.getDeptType()) && "11302".equals(user.getRoleLevel())){
			String sessionid = session.getId(); //获取sessionid
	        Cookie cookie = new Cookie("JSESSIONID", sessionid); //new一个cookie，cookie的名字是JSESSIONID跟带id的cookie一样
	        cookie.setPath(request.getContextPath()); //设置cookie应用范围。getContextPath是获取当前项目的名字。
	        cookie.setMaxAge(30*24*60*60); //设置有效时间7天免登陆
	        response.addCookie(cookie);//用这个cookie把带id的cookie覆盖掉
		}
		
		if (!(requestUrl.contains("login")) && user == null) {
			if(requestUrl.contains(ConfigUtil.getConfigKey("REDIRECT_FLAG"))){
				response.sendRedirect(phone_logout_page);
			}else{
				response.sendRedirect(logout_page);
			}
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
