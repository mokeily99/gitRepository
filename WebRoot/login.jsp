<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en" class="no-js">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>天翼云呼</title>

<link rel="stylesheet" type="text/css" href="<%=webpath%>/login.css" />
<link rel="shortcut icon" href="<%=webpath%>/common/ui/hAdmin/img/favicon.ico" type="image/x-icon"> 

<script type="text/javascript" src="<%=webpath%>/common/js/jquery-1.10.2.min.js"></script>

<script type="text/javascript">
	function loginSubmit(){
		var userName = $("#username").val();
		var password = $("#password").val();
		if(userName == ""){
			$("#username").css("border-bottom", "0.0625rem solid red");
			$("#msg").text("账号不能为空！");
			return;
		}else{
			$("#username").css("border-bottom", "0.0625rem solid #347ebc");
		}
		if(password == ""){
			$("#password").css("border-bottom", "0.0625rem solid red");
			$("#msg").text("密码不能为空！");
			return;
		}else{
			$("#password").css("border-bottom", "0.0625rem solid #347ebc");
		}
		
		$("#login_form").submit();
	}
</script>
</head>
<body>
	<div style="width:100%;height:80px;background-color:white;"><img style="margin-left:300px;margin-top:10px;height:70px;" src="<%=webpath%>/common/img/logo.png"></img></div>
	<div class="bg1"></div>
	<div class="gyl">
		天翼云呼系统
		<div class="gy2">追求品质，只争朝夕，全力以赴，矢志不移</div>
	</div>
	<div class="bg" style="font-size:14px;">
		<div class="wel">用户登录</div>
		<form id="login_form" action="<%=webpath%>/login/userLogin.action" method="post">
			<div class="user">
				<div id="yonghu" style="">用户名</div>
				<input type="text" name="username" id="username" placeholder="请输入用户名">
			</div>
			<div class="password">
				<div id="yonghu">密&nbsp;&nbsp;&nbsp;&nbsp;码</div>
				<input class="" type="password" id="password" name="password" placeholder="请输入密码">
			</div>
		</form>
		<input class="btn" type="button" value="登录" onclick="loginSubmit();">
		<span id="msg" style="color: red;position:absolute;top:19rem;right:9.3rem;font-size:12px;">${msg}</span>
	</div>
<div style="width:100%;height:80px;background-color:white;position:absolute;bottom:0px;">
	<div class="loginbm">当前系统版本:v1.0.0 </div>
</div>
</body>
</html>