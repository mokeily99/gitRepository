<%@page import="com.yl.common.user.pojo.UserView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
	UserView user = (UserView) session.getAttribute("userInfo");
	String userLevel = user.getRoleLevel();
%>
<!DOCTYPE html>
<html>
<head>

<title>账户信息</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/account/list/list.js"></script>
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
					<label class="layui-form-label" style="width:98px;">部门名称</label>
					<div class="layui-input-inline">
						<input type="text" id="dept_name" name="dept_name" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_account_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			<%if("10201".equals(user.getRoleLevel())){ %>
				<div style="width:99%;margin:auto;">
					<div class="layui-btn-group">
				    	<button class="layui-btn" id="add_fee_btn">账户金额修改</button>
				  	</div>
			  	</div>
			<%} %>
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="account_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_fee_div"  style="display:none;">
	<form lay-filter="add_fee_form" class="layui-form" action="" id="add_fee_form">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">部门账户名</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_dept_name" id="add_dept_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;" disabled="disabled">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">余额</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_balance_fee" id="add_balance_fee" required  lay-verify="required|fee" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_fee_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>