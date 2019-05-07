<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>短信模板编辑</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/smsManager/mould/mould.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset"
			style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">模板标题</label>
					<div class="layui-input-inline">
						<input type="text" id="mould_title" name="mould_title"
							autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal"
							id="query_mould_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button"
			style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>

			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
					<button class="layui-btn" id="add_mould_btn">增加</button>
					<button class="layui-btn" id="edit_mould_btn">编辑</button>
					<button class="layui-btn" id="del_mould_btn">删除</button>
				</div>
			</div>
			<div style="width:99%;margin:auto;">
				<table class="layui-hide" id="mould_grid_list"></table>
			</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_mould_div" style="display:none;">
	<form lay-filter="add_mould_form" class="layui-form" action="" id="add_mould_form">
		<div class="layui-form-item" style="margin-top:15px;">
			<label class="layui-form-label">模板标题</label>
			<div class="layui-input-block">
				<input type="text" placeholder="请输标题" name="add_mould_title" id="add_mould_title" required lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
			</div>
		</div>
		<div class="layui-form-item" style="margin-top:15px;width: 411px;">
			<label class="layui-form-label">模板类型</label>
			<div class="layui-input-block">
				<select id="add_mould_type" name="add_mould_type" lay-verify="required"></select>
			</div>
		</div>
		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">模板内容</label>
			<div class="layui-input-block">
				<textarea placeholder="请输入内容" class="layui-textarea" id="add_mould_content" name="add_mould_content" style="width:300px;"></textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block"
				style="float:right;margin-right:20px;margin-top:15px;">
				<a class="layui-btn" lay-submit lay-filter="add_mould_form_sub">提交</a>
				<a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
			</div>
		</div>
	</form>
</div>

<div id="edit_mould_div" style="display:none;">
	<form lay-filter="edit_mould_form" class="layui-form" action="" id="edit_mould_form">
		<input type="hidden" id="edit_mould_id" name="edit_mould_id">
		<div class="layui-form-item" style="margin-top:15px;">
			<label class="layui-form-label">模板标题</label>
			<div class="layui-input-block">
				<input type="text" placeholder="请输标题" name="edit_mould_title" id="edit_mould_title" required lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
			</div>
		</div>
		<div class="layui-form-item" style="margin-top:15px;width: 411px;">
			<label class="layui-form-label">模板类型</label>
			<div class="layui-input-block">
				<select id="edit_mould_type" name="edit_mould_type" lay-verify="required" disabled="disabled"></select>
			</div>
		</div>
		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">模板内容</label>
			<div class="layui-input-block">
				<textarea placeholder="请输入内容" class="layui-textarea" id="edit_mould_content" name="edit_mould_content" style="width:300px;"></textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block"
				style="float:right;margin-right:20px;margin-top:15px;">
				<a class="layui-btn" lay-submit lay-filter="edit_mould_form_sub">提交</a>
				<a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
			</div>
		</div>
	</form>
</div>
