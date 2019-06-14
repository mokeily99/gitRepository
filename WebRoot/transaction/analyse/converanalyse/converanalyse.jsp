<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
	UserView user = (UserView) session.getAttribute("userInfo");
	String userLevel = user.getRoleLevel();
%>
<!DOCTYPE html>
<html>
<head>

<title>通话分析</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />

<script type="text/javascript" src="<%=webpath%>/transaction/analyse/converanalyse/converanalyse.js"></script>
<script type="text/javascript" src="<%=webpath%>/transaction/analyse/converanalyse/converTalkAn.js"></script>

<script type="text/javascript">
	var userLevel = '<%=userLevel%>';
</script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">开始时间</label>
					<div class="layui-input-inline">
						<input type="text" class="layui-input" id="begin_query_date" name="begin_query_date">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">结束时间</label>
					<div class="layui-input-inline">
						<input type="text" class="layui-input" id="end_query_date" name="end_query_date">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_black_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			
		  	<div style="width:49%;float:left;margin-left:10px;">
			  	<table class="layui-hide" id="conver_an_grid_list"></table>
		  	</div>
		  	<div style="width:49%;float:left;margin-left:10px;">
				<div id="conver_talk_an"></div>
		  	</div>
		</fieldset>
	</div>
</body>
</html>