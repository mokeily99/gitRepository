<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
	UserView user = (UserView) session.getAttribute("userInfo");
	String maxaccept = user.getMaxaccept();
%>
<!DOCTYPE html>
<html>
<head>

<title>弹屏管理</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<style type="text/css">
	.call_phone{
		font-size:20px;font-weight:bold;padding:10px;text-align:center;color:red;
	}
</style>
</head>


<body>
	<div id="show_screen_div">
		<div id="call_phone" class="call_phone"></div>
		<hr class="layui-bg-red">
	</div>
</body>
</html>