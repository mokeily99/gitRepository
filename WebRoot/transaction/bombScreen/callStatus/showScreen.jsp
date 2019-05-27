<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
	UserView user = (UserView) session.getAttribute("userInfo");
	String maxaccept = user.getMaxaccept();
%>
sdfdsfsdf