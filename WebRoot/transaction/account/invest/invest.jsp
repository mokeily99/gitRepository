<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>账户充值</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/account/invest/invest.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">账户</label>
					<div class="layui-input-inline">
						<input type="text" id="black_phone" name="black_phone" autocomplete="off" class="layui-input">
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
			
			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
			    	<button class="layui-btn" id="add_black_btn">充值</button>
				    <button class="layui-btn" id="edit_black_btn">扣费</button>
			  	</div>
		  	</div>
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="black_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_black_div"  style="display:none;">
	<form lay-filter="add_black_form" class="layui-form" action="" id="add_black_form">
	  <input type="hidden" id="add_black_id" name="add_black_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">账户</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入手机号码" name="add_black_phone" id="add_black_phone" required  lay-verify="required|phone" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item layui-form-text">
	    <label class="layui-form-label">备注</label>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入内容" class="layui-textarea" id="add_black_reason" name="add_black_reason" style="width:300px;"></textarea>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_black_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="edit_black_div"  style="display:none;">
	<form lay-filter="edit_black_form" class="layui-form" action="" id="edit_black_form">
	  <input type="hidden" id="edit_black_id" name="edit_black_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">黑名单号码</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入手机号码" name="edit_black_phone" id="edit_black_phone" required  lay-verify="required|phone" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item layui-form-text">
	    <label class="layui-form-label">加黑原因</label>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入内容" class="layui-textarea" id="edit_black_reason" name="edit_black_reason" style="width:300px;"></textarea>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_black_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>
