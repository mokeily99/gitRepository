<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>人员管理</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<link href="<%=webpath%>/common/ui/extends-layui-tree/eleTree.css" rel="stylesheet">
<script type="text/javascript" src="<%=webpath%>/transaction/system/role/role.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			<div style="width:99%;margin:auto;">
				<div>
					<div class="layui-btn-group">
				    	<button class="layui-btn" id="add_role_btn">角色新增</button>
					    <button class="layui-btn" id="edit_role_btn">角色编辑</button>
					    <button class="layui-btn" id="del_role_btn">角色删除</button>
					    <button class="layui-btn" id="save_menu_btn" onclick="save_role_menu();">菜单保存</button>
				  	</div>
			  	</div>
			  	<div style="width:49%;float:left;">
				  	<table class="layui-hide" id="role_grid_list"  lay-filter="role_grid_list"></table>
			  	</div>
			  	<div style="width:49%;float:left;border:solid 1px rgba(230, 230, 230, 1);margin-left:14px;margin:10px;">
				  	<div class="eleTree menu_elem"></div>
			  	</div>
			</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_role_div" style="display:none;">
	<form lay-filter="add_role_form" class="layui-form" action="" id="add_role_form">
	  <input type="hidden" id="add_role_id" name="add_role_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">角色名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_role_name" id="add_role_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">角色描述</label>
	    <div class="layui-input-block">
	      <input type="password" name="add_role_des" id="add_role_des" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">角色类型</label>
	    <div class="layui-input-block">
	      <select id="add_role_type" name="add_role_type" lay-verify="required"></select>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_role_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="edit_role_div" style="display:none;">
	<form lay-filter="edit_role_form" class="layui-form" action="" id="edit_role_form">
	  <input type="hidden" id="edit_role_id" name="edit_role_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">角色名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_role_name" id="edit_role_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">角色描述</label>
	    <div class="layui-input-block">
	      <input type="password" name="edit_role_des" id="edit_role_des" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">角色类型</label>
	    <div class="layui-input-block">
	      <select id="edit_role_type" name="edit_role_type" lay-verify="required"></select>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_role_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>