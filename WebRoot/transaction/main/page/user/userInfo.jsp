<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yl.common.user.pojo.UserView"%>
<%
	String webpath = request.getContextPath();
	UserView user = (UserView) request.getSession().getAttribute("userInfo");
	request.setAttribute("uv", user);
	String maxaccept = user.getMaxaccept();
	String name = user.getUserName();
		
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>个人资料</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta charset="utf-8">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="../../layui/css/layui.css" media="all" />
	<link rel="stylesheet" href="../../css/user.css" media="all" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" ; name="viewport" />
	
	<jsp:include page="../../../../common/jsp/commonContent.jsp" flush="true" />
	<script type="text/javascript" src="<%=webpath%>/transaction/main/page/user/userInfo.js"></script>
	<script>
	  	var webpath = '<%=webpath%>';
		var roleLevel = '<%=user.getRoleLevel()%>';
		var roleCode = '<%=user.getUserRole()%>';
		var photoHead = '<%=user.getPhotoHead()%>';
	</script>
  </head>
  
 <body class="childrenBody">
	<form class="layui-form">
		<input type="hidden" id="maxaccept" name="maxaccept" value="<%=user.getMaxaccept()%>">
		<div class="user_left">
			<div class="layui-form-item">
			    <label class="layui-form-label">账号</label>
			    <div class="layui-input-block">
			    	<input type="text" disabled class="layui-input layui-disabled" id="user_account" name="user_account" value="<%=user.getUserAccount()%>">
			    </div>
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">密码</label>
			    <div class="layui-input-block">
			    	<input type="password" disabled class="layui-input layui-disabled" id="user_pwd" name="user_pwd" value="******">
			    </div>
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">所属部门</label>
			    <div class="layui-input-block">
		    		<input type="text" disabled class="layui-input layui-disabled" id="user_dept" name="user_dept" value="<%=user.getDeptName()%>">
			    </div>
			</div>
			
			<div class="layui-form-item">
			    <label class="layui-form-label">角色</label>
			    <div class="layui-input-block">
		    		<input type="text" disabled class="layui-input layui-disabled" id="user_role" name="user_role" value="<%=user.getRoleName()%>">
			    </div>
			</div>
			<%if("11603".equals(user.getRoleLevel())) {%>
				<div class="layui-form-item">
				    <label class="layui-form-label">所属分类</label>
				    <div class="layui-input-block">
			    		<select id="deformity_type" name="deformity_type" lay-verify="required"></select>
				    </div>
				</div>
	    	<%}%>
			<div class="layui-form-item">
			    <label class="layui-form-label">用户姓名</label>
			    <div class="layui-input-block">
			    	<input type="text" placeholder="请输入用户姓名" class="layui-input" id="userName" name="userName" value="<%=user.getUserName()%>">
			    </div>
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">联系电话</label>
			    <div class="layui-input-block">
			    	<input type="tel" placeholder="请输入手机号码" lay-verify="connPhone" class="layui-input" id="userPhone" name="userPhone" value="<%=user.getPhone()%>">
			    </div>
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">家庭住址</label>
			    <div class="layui-input-block">
			    	<input type="text" placeholder="请输入住址" class="layui-input" id="userAddress" name="userAddress" value="<%=user.getAddress()%>">
			    </div>
			</div>
		</div>
		<div class="user_right">
			<a class="layui-btn test" name="user_face" id="head_upload_input" >掐指一算，我要换一个头像了</a>
			<p>头像数据</p>
			<img src="" class="layui-circle" id="userFace">
		</div>
		<div class="layui-form-item" style="margin-left: 5%;">
		    <div class="layui-input-block">
		    	<a class="layui-btn" lay-submit lay-filter="change_user">提交</a>
				<button type="reset" class="layui-btn layui-btn-danger">恢复</button>
		    </div>
		</div>
	</form>
</body>
</html>
