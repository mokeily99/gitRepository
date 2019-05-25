<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>通话记录查询</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/conver/converQuery/converQuery.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">主叫号码</label>
					<div class="layui-input-inline">
						<input type="text" id="caller_phone" name="caller_phone" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">被叫号码</label>
					<div class="layui-input-inline">
						<input type="text" id="called_phone" name="called_phone" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_conver_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			
			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
				    <button class="layui-btn" id="send_sms_btn">短信发送</button>
			  	</div>
		  	</div>
		  	
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="conver_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="send_sms_div" style="display:none;">
	<form lay-filter="send_sms_form" class="layui-form" action="" id="esend_sms_form">
		<input type="hidden" id="send_phones" name="send_phones">
		<div class="layui-form-item" style="margin-top:15px;width: 411px;">
			<label class="layui-form-label">发送方式</label>
			<div class="layui-input-block">
				<select id="send_sms_type" name="send_sms_type" lay-verify="required" lay-filter="send_type"></select>
			</div>
		</div>
		<div class="layui-form-item" id="send_sms_date_div" style="margin-top:15px;width: 411px;display:none;">
			<label class="layui-form-label">发送日期</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" id="send_sms_date" name="send_sms_date">
			</div>
		</div>
		<div class="layui-form-item" style="margin-top:15px;width: 411px;">
			<label class="layui-form-label">插入模板</label>
			<div class="layui-input-block">
				<select id="sms_mould_type" name="sms_mould_type" lay-filter="sms_mould"></select>
			</div>
		</div>
		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">短信内容</label>
			<div class="layui-input-block">
				<textarea class="layui-textarea" id="send_sms_content" name="send_sms_content" lay-verify="required" style="width:300px;"></textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block"
				style="float:right;margin-right:20px;margin-top:15px;">
				<a class="layui-btn" lay-submit lay-filter="send_sms_form_sub">提交</a>
				<a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
			</div>
		</div>
	</form>
</div>
