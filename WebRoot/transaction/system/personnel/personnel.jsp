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
<script type="text/javascript" src="<%=webpath%>/common/js/jquery-easyui-1.5.5.1/jquery.min.js"></script>
<link href="<%=webpath%>/common/ui/treeselect/assets/layui/css/layui.css" rel="stylesheet">
<script src="<%=webpath%>/common/ui/treeselect/assets/layui/layui.js"></script>
<script src="<%=webpath%>/common/ui/treeselect/module/common.js"></script>
<script type="text/javascript" src="<%=webpath%>/transaction/system/personnel/personnel.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label">姓名</label>
					<div class="layui-input-inline">
						<input type="tel" id="user_name" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">角色</label>
					<div class="layui-input-inline">
						<select id="role_level" lay-verify="required"></select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_personnel_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			
			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
			    	<button class="layui-btn" id="add_personnel_btn">增加</button>
				    <button class="layui-btn" id="edit_personnel_btn">编辑</button>
				    <button class="layui-btn" id="del_personnel_btn">删除</button>
			  	</div>
		  	</div>
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="personnel_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_personnel_div"  style="display:none;">
	<form lay-filter="add_personnel_form" class="layui-form" action="" id="add_personnel_form">
	  <input type="hidden" id="add_personnel_pid" name="add_personnel_pid">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">登录账号</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_personnel_account" id="add_personnel_account" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">登录密码</label>
	    <div class="layui-input-block">
	      <input type="password" name="add_personnel_pwd" id="add_personnel_pwd" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">用户姓名</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_personnel_name" id="add_personnel_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">联系电话</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_personnel_phone" id="add_personnel_phone" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">联系地址</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_personnel_address" id="add_personnel_address" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="width: 411px;">
          <label for="" class="layui-form-label">所属部门</label>
          <div class="layui-input-block">
              <input type="text" id="add_personnel_dept" name="add_personnel_dept" lay-filter="tree"  lay-verify="required" class="layui-input">
          </div>
      </div>
      <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">所属角色</label>
	    <div class="layui-input-block">
	      <select id="add_personnel_role" name="add_personnel_role" lay-verify="required"></select>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_personnel_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="edit_personnel_div"  style="display:none;">
	<form lay-filter="edit_personnel_form" class="layui-form" action="" id="edit_personnel_form">
	  <input type="hidden" id="edit_personnel_id" name="edit_personnel_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">登录账号</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_personnel_account" id="edit_personnel_account" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">登录密码</label>
	    <div class="layui-input-block">
	      <input type="password" name="edit_personnel_pwd" id="edit_personnel_pwd" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">用户姓名</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_personnel_name" id="edit_personnel_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">联系电话</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_personnel_phone" id="edit_personnel_phone" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">联系地址</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_personnel_address" id="edit_personnel_address" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="width: 411px;">
          <label for="" class="layui-form-label">所属部门</label>
          <div class="layui-input-block">
              <input type="text" id="edit_personnel_dept" name="edit_personnel_dept" lay-filter="edit_personnel_dept"  lay-verify="required" class="layui-input">
          </div>
      </div>
      <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">所属角色</label>
	    <div class="layui-input-block">
	      <select id="edit_personnel_role" name="edit_personnel_role" lay-verify="required"></select>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_personnel_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>
