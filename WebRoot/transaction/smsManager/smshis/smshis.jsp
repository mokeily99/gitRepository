<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>已发送短信记录查询</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/smsManager/smshis/smshis.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">手机号码</label>
					<div class="layui-input-inline">
						<input type="text" id="sms_phone" name="sms_phone" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline" id="send_flag_div">
					<label class="layui-form-label" style="width:98px;">发送结果</label>
					<div class="layui-input-inline">
						<select id="send_flag" lay-verify="required"></select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_sms_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="sms_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>
