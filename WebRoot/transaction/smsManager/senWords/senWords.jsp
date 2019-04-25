<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>敏感词管理</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/smsManager/senWords/senWords.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">敏感词模糊查询</label>
					<div class="layui-input-inline">
						<input type="text" id="sen_words" name="sen_words" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_senwords_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			
			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
			    	<button class="layui-btn" id="add_senwords_btn">增加</button>
				    <button class="layui-btn" id="edit_senwords_btn">编辑</button>
				    <button class="layui-btn" id="del_senwords_btn">删除</button>
			  	</div>
		  	</div>
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="senwords_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_senwords_div"  style="display:none;">
	<form lay-filter="add_senwords_form" class="layui-form" action="" id="add_senwords_form">
	  <input type="hidden" id="add_senwords_id" name="add_senwords_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">敏感词</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_senwords" id="add_senwords" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_senwords_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="edit_senwords_div"  style="display:none;">
	<form lay-filter="edit_senwords_form" class="layui-form" action="" id="edit_senwords_form">
	  <input type="hidden" id="edit_senwords_id" name="edit_senwords_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">敏感词</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_senwords" id="edit_senwords" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_senwords_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>
