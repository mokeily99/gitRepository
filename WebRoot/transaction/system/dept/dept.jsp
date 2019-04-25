<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();

	UserView uv = new UserView();
	String maxaccept = uv.getMaxaccept();
	String name = uv.getUserName();
%>
<!DOCTYPE html>
<html>
<head>

<title>部门管理</title>
<jsp:include page="../../../common/jsp/commonContentWithoutCheck.jsp" flush="true" />
<link href="<%=webpath%>/common/ui/extends-layui-tree/eleTree.css" rel="stylesheet">

<script type="text/javascript" src="<%=webpath%>/transaction/system/dept/dept.js"></script>

<script type="text/javascript">
	var user_id = '<%=maxaccept%>';
	var user_name = '<%=name%>';

	$(function() {
		//表单组件渲染
		layui.use('form', function() {
			var form = layui.form;
			form.render();
		});
	});
</script>
</head>


<body>
	<div class="page_panel">
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button"
			style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>

			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
					<button class="layui-btn" id="add_dept_btn">部门添加</button>
					<button class="layui-btn" id="edit_dept_btn">部门编辑</button>
					<button class="layui-btn" id="del_dept_btn">部门删除</button>
				</div>
			</div>
			<div class="content1" style="margin-top:10px;">
				<div class="eleTree dept_elem"></div>
			</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_dept_div"  style="display:none;">
	<form lay-filter="add_dept_form" class="layui-form" action="" id="add_dept_form">
	  <input type="hidden" id="add_dept_pid" name="add_dept_pid">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">部门名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_dept_name" id="add_dept_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">部门描述</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_dept_des" id="add_dept_des" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_dept_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="edit_dept_div"  style="display:none;">
	<form lay-filter="edit_dept_form" class="layui-form" action="" id="edit_dept_form">
	  <input type="hidden" id="edit_dept_id" name="edit_dept_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">部门名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_dept_name" id="edit_dept_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">部门描述</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_dept_des" id="edit_dept_des" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_dept_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>