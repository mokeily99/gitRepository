<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>菜单管理</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<link href="<%=webpath%>/common/ui/extends-layui-tree/eleTree.css" rel="stylesheet">
<script type="text/javascript" src="<%=webpath%>/transaction/system/menu/menu.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			<div style="width:99%;margin:auto;">
				<div>
					<div class="layui-btn-group">
				    	<button class="layui-btn" id="add_menu_btn">菜单新增</button>
					    <button class="layui-btn" id="edit_menu_btn">菜单编辑</button>
					    <button class="layui-btn" id="del_menu_btn">菜单删除</button>
				  	</div>
			  	</div>
			  	<div class="content1" style="margin-top:10px;">
					<div class="eleTree" id="menu_tree" lay-filter="menu_tree"></div>
				</div>
			</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_menu_div" style="display:none;">
	<form lay-filter="add_menu_form" class="layui-form" action="" id="add_menu_form">
	  <input type="hidden" id="add_menu_pid" name="add_menu_pid">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">菜单名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_menu_name" id="add_menu_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">菜单地址</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_menu_adr" id="add_menu_adr" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_menu_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="edit_menu_div" style="display:none;">
	<form lay-filter="edit_menu_form" class="layui-form" action="" id="edit_menu_form">
	  <input type="hidden" id="edit_menu_id" name="edit_menu_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">菜单名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_menu_name" id="edit_menu_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">菜单地址</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_menu_adr" id="edit_menu_adr" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_menu_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>