<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.yl.common.user.pojo.UserView"%>
<%
	String webpath = request.getContextPath();
	
	UserView user = (UserView) session.getAttribute("userInfo");
	String userame = user.getUserName();
	String maxaccept = user.getMaxaccept();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>首页</title>

<link rel="icon" href="favicon.ico">
<link rel="stylesheet" href="<%=webpath%>/transaction/main/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="<%=webpath%>/transaction/main/css/font_eolqem241z66flxr.css" media="all" />
<link rel="stylesheet" href="<%=webpath%>/transaction/main/css/main.css" media="all" />

<script type="text/javascript" src="<%=webpath%>/common/js/jquery-1.10.2.min.js"></script>
<script src="<%=webpath%>/transaction/main/bar.js"></script>

<style type="text/css">
	a{
		color:white;
	}
	.seat_class:active{
		color:red;
	}
</style>
<script type="text/javascript">
	var webpath = '<%=webpath%>';
	var seatID = '<%=maxaccept%>';
</script>

</head>

<body class="main_body">
	<div class="layui-layout layui-layout-admin">
		<!-- 顶部 -->
		<div class="layui-header header">
			<div class="layui-main">
				<a href="#" class="logo">天翼云呼</a>
				<!-- 天气信息 -->
				<div class="weather" pc>
					<div id="tp-weather-widget"></div>
					<script>(function(T, h, i, n, k, P, a, g, e) {
							g = function() {
								P = h.createElement(i);
								a = h.getElementsByTagName(i)[0];
								P.src = k;
								P.charset = "utf-8";
								P.async = 1;a.parentNode.insertBefore(P, a)
							};
							T["ThinkPageWeatherWidgetObject"] = n;T[n] || (T[n] = function() {
								(T[n].q = T[n].q || []).push(arguments)
							});
							T[n].l = +new Date();
							if (T.attachEvent) {
								T.attachEvent("onload", g)
							} else {
								T.addEventListener("load", g, false)
							}
						}(window, document, "script", "tpwidget", webpath+"/transaction/main/js/widget-cc5d550.js"))
					</script>
					<script>
						tpwidget("init", {
				            "flavor": "slim",
				            "location": "WX4FBXXFKE4F",
				            "geolocation": "disabled",
				            "language": "zh-chs",
				            "unit": "c",
				            "theme": "chameleon",
				            "container": "tp-weather-widget",
				            "bubble": "enabled",
				            "alarmType": "badge",
				            "color": "#F47837",
				            "uid": "U605DCADA4",
				            "hash": "78f46a1198d54dafa0cda717efa717a9"
				        });
						tpwidget("show");
					</script>
					<script>
						function loginOut(){
						window.location.href = webpath + "/login.jsp";
						}
					</script>
				</div>
				<div style="float: left; height: 100%; width: 434px;">
					<div style="color:white;float:left;margin:20px;"><img id="seat_free_busy_img" src="<%=webpath%>/common/img/busy.png" style="margin-top:-10px;"/></div>
					<div style="color:white;float:left;margin:20px;"><a class="seat_class" href="javascript:setFree()">置闲</a></div>
					<div style="color:white;float:left;margin:20px;"><a class="seat_class" href="javascript:setBusy()">置忙</a></div>
				</div>
				<!-- 顶部右侧菜单 -->
				<ul class="layui-nav top_menu">
					<li class="layui-nav-item showNotice" id="showNotice" pc><a
						href="javascript:;"><i class="iconfont icon-gonggao"></i><cite>系统提醒</cite></a>
					</li>
					<li class="layui-nav-item" mobile><a href="javascript:;"
						data-url="page/user/changePwd.html"><i
							class="iconfont icon-shezhi1" data-icon="icon-shezhi1"></i><cite>设置</cite></a>
					</li>
					<li class="layui-nav-item" mobile><a href="javascript:;"><i
							class="iconfont icon-loginout"></i> 退出</a></li>
					<li class="layui-nav-item lockcms" pc><a href="javascript:;"><i
							class="iconfont icon-lock1"></i><cite>锁屏</cite></a></li>
					<li class="layui-nav-item" pc>
						<a href="javascript:;">
							<%if(StringUtils.isNotBlank(user.getPhotoHead())){ %>
								<img src="/facePic/<%=user.getPhotoHead()%>" class="layui-circle" width="35" height="35">
							<%} else {%>
								<img src="<%=webpath%>/transaction/main/images/face.png" class="layui-circle" width="35" height="35">
							<%}%>
							<cite><%=userame %></cite>
						</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="javascript:;" data-url="page/user/userInfo.jsp"><i
									class="iconfont icon-zhanghu" data-icon="icon-zhanghu"></i><cite>个人资料</cite></a>
							</dd>
							<dd>
								<a href="javascript:;" data-url="page/user/changePwd.jsp"><i
									class="iconfont icon-shezhi1" data-icon="icon-shezhi1"></i><cite>修改密码</cite></a>
							</dd>
							<dd>
								<a href="javascript:loginOut();"><i class="iconfont icon-loginout"></i><cite>退出</cite></a>
							</dd>
						</dl>
					</li>
				</ul>
			</div>
		</div>
		<!-- 左侧导航 -->
		<div class="layui-side layui-bg-black">
			<div class="user-photo">
				<%if(StringUtils.isNotBlank(user.getPhotoHead())){ %>
					<a class="img" title="我的头像"><img src="/facePic/<%=user.getPhotoHead()%>"></a>
				<%} else {%>
					<a class="img" title="我的头像"><img src="<%=webpath%>/transaction/main/images/face.png"></a>
				<%}%>
				<p>
					你好！<span class="userName"><%=userame %></span>, 欢迎登录
				</p>
			</div>
			<div class="navBar layui-side-scroll"></div>
		</div>
		<!-- 右侧内容 -->
		<div class="layui-body layui-form">
			<div class="layui-tab marg0" lay-filter="bodyTab">
				<ul class="layui-tab-title top_tab">
					<li class="layui-this" lay-id=""><i
						class="iconfont icon-computer"></i> <cite>后台首页</cite></li>
				</ul>
				<div class="layui-tab-content clildFrame">
					<div class="layui-tab-item layui-show">
						<iframe src="page/main.jsp"></iframe>
					</div>
				</div>
			</div>
		</div>
		<!-- 底部 -->
		<!-- <div class="layui-footer footer">
			<p>
				copyright @2017 请叫我马哥 更多模板：<a href="http://www.mycodes.net/"
					target="_blank">源码之家</a> <a onclick="donation()"
					class="layui-btn layui-btn-danger l·ayui-btn-small">捐赠作者</a>
			</p>
		</div> -->
	</div>

	<!-- 锁屏 -->
	<div class="admin-header-lock" id="lock-box" style="display: none;">
		<div class="admin-header-lock-img">
			<%if(StringUtils.isNotBlank(user.getPhotoHead())){ %>
				<img src="/facePic/<%=user.getPhotoHead()%>">
			<%} else {%>
				<img src="<%=webpath%>/transaction/main/images/face.png">
			<%}%>
		</div>
		<div class="admin-header-lock-name" id="lockUserName"><%=userame %></div>
		<div class="input_btn">
			<input type="password" class="admin-header-lock-input layui-input"
				placeholder="请输入密码解锁.." name="lockPwd" id="lockPwd" />
			<button class="layui-btn" id="unlock">解锁</button>
		</div>
		<p>请输入“123456”，否则不会解锁成功哦！！！</p>
	</div>
	<!-- 移动导航 -->
	<div class="site-tree-mobile layui-hide">
		<i class="layui-icon">&#xe602;</i>
	</div>
	<div class="site-mobile-shade"></div>

	<script src="<%=webpath%>/common/ui/treeselect/assets/layui/layui.js"></script>
	<script type="text/javascript" src="<%=webpath%>/common/ui/layui/layui.extends.js"></script>
	<!-- <script type="text/javascript" src="js/nav.js"></script> -->
	<script type="text/javascript" src="js/leftNav.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
	<script src="<%=webpath%>/transaction/bombScreen/callStatus/callStatus.js"></script>
</body>
</html>

<!-- 弹屏 -->
<div id="show_screen_div" style="display:none;">
	<div id="call_phone" style="font-size:36px;font-weight:bold;padding:10px 10px 0px;text-align:center;color:cadetblue;"></div>
	<fieldset class="layui-elem-field site-demo-button" style=";padding:20px;border:1px solid rgba(26, 159, 147, 1);">
		<legend>工单创建</legend>
		
		<form class="layui-form layui-form-pane" action="">
		   	<div class="layui-inline">
			    <label class="layui-form-label">客户姓名</label>
			    <div class="layui-input-inline">
			      <input type="text" name="cust_name" id="cust_name" placeholder="请输入" autocomplete="off" class="layui-input">
			    </div>
		  	</div>
		  	<div class="layui-inline" style="float:right;">
			    <label class="layui-form-label">联系电话</label>
			    <div class="layui-input-inline">
			      <input type="text" name="conn_phone" id="conn_phone" lay-verify="required|phone" placeholder="请输入" autocomplete="off" class="layui-input">
			    </div>
		  	</div>
		  	<div class="layui-form-item" style="margin-top:10px;">
			    <label class="layui-form-label">住址</label>
			    <div class="layui-input-block">
			      <input type="text" name="title" autocomplete="off" placeholder="请输入" class="layui-input">
			    </div>
  			</div>
  			<div class="layui-form-item layui-form-text">
			    <label class="layui-form-label">工单备注</label>
			    <div class="layui-input-block">
			      <textarea class="layui-textarea"></textarea>
			    </div>
			</div>
			<div class="layui-inline">
			    <label class="layui-form-label">是否派单</label>
			    <div class="layui-input-block">
			      <select name="is_send" lay-filter="is_send">
			        <option value="0" selected="selected">否</option>
			        <option value="1" >是</option>
			      </select>
			    </div>
		  	</div>
		  	<div class="layui-inline" style="float:right;">
			    <label class="layui-form-label">派发人员</label>
			    <div class="layui-input-inline">
			    	<select id="order_send_opr" name="order_send_opr" style="width:300px;"></select>
			    </div>
		  	</div>
		</form>
	</fieldset>
</div>