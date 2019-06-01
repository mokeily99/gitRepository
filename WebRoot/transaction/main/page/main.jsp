<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.yl.common.user.pojo.UserView"%>
<%
	String webpath = request.getContextPath();
	
	UserView user = (UserView) session.getAttribute("userInfo");
	String userame = user.getUserName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>首页--layui后台管理模板</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="../layui/css/layui.css" media="all" />
	<link rel="stylesheet" href="../css/font_eolqem241z66flxr.css" media="all" />
	<link rel="stylesheet" href="../css/main.css" media="all" />
	<script type="text/javascript" src="../layui/layui.js"></script>
	<script src="<%=webpath%>/common/ui/hAdmin/js/jquery.min.js?v=2.1.4"></script>
	
	<script src="<%=webpath%>/common/js/Highcharts-6.1.0/highcharts.js"></script>
	<script src="<%=webpath%>/common/js/Highcharts-6.1.0/modules/exporting.js"></script>
	<script src="<%=webpath%>/common/js/Highcharts-6.1.0/highcharts-zh_CN.js"></script>
	<script type="text/javascript" src="main.js"></script>
	<script type="text/javascript">
		var webpath = '<%=webpath%>';
		$(function(){
			getChart();
		})
	</script>
</head>
<body class="childrenBody">
	<div class="panel_box row" >
		<div class="panel col">
			<div>
				<div class="panel_icon">
					<i class="layui-icon" data-icon="&#xe63a;">&#xe63a;</i>
				</div>
				<div class="panel_word sms_count">
					<span></span>
					<cite>短信总数</cite>
				</div>
			</div>
		</div>
		<div class="panel col">
			<a href="javascript:;" data-url="<%=webpath%>/transaction/smsManager/smshis/smshis.jsp">
				<div class="panel_icon" style="background-color:#FF5722;">
					<i class="layui-icon" data-icon="&#xe605;">&#xe605;</i>
				</div>
			<div class="panel_word send_sms_count">
				<span></span>
				<cite>已发短信</cite>
			</div>
			</a>
		</div>
		<div class="panel col">
			<a href="javascript:;" data-url="<%=webpath%>/transaction/smsManager/smslist/smslist.jsp">
				<div class="panel_icon" style="background-color:#009688;">
					<i class="layui-icon" data-icon="&#x1006;">&#x1006;</i>
				</div>
				<div class="panel_word un_sms_count">
					<span></span>
					<cite>未发短信</cite>
				</div>
			</a>
		</div>
		<div class="panel col">
			<div>
				<div class="panel_icon" style="background-color:#5FB878;">
					<i class="layui-icon" data-icon="&#xe63b;">&#xe63b;</i>
				</div>
				<div class="panel_word conver_num">
					<span></span>
					<cite>通话总数</cite>
				</div>
			</div>
		</div>
		<div class="panel col">
			<a href="javascript:;" data-url="<%=webpath%>/transaction/conver/converQuery/converQuery.jsp?callForward=0">
				<div class="panel_icon" style="background-color:#FF5722;">
					<i class="layui-icon" data-icon="&#xe603;">&#xe603;</i>
				</div>
				<div class="panel_word into_conver_num">
					<span></span>
					<cite>呼入总数</cite>
				</div>
			</a>
		</div>
		<div class="panel col max_panel">
			<a href="javascript:;" data-url="<%=webpath%>/transaction/conver/converQuery/converQuery.jsp?callForward=1">
				<div class="panel_icon" style="background-color:#009688;">
					<i class="layui-icon" data-icon="&#xe602;">&#xe602;</i>
				</div>
				<div class="panel_word out_conver_num">
					<span></span>
					<cite>呼出总数</cite>
				</div>
			</a>
		</div>
	</div>
	
	<!-- HighCharts图表 -->
	<div class="row" style="width:100%;height:20%;">
		<div class="sysNotice col">
			<blockquote class="layui-elem-quote title">工单量分析</blockquote>
			<div id="order_type_num_an" style="width:100%;height:295px;float:left;display:none;"></div>
		</div>
		<div class="sysNotice col">
			<blockquote class="layui-elem-quote title">短信类型分析</blockquote>
			<div id="patientCount" style="width:100%;height:295px;float:left"></div>
		</div>
		<div class="sysNotice col">
			<blockquote class="layui-elem-quote title">通话分析</blockquote>
			<div id="question" style="width:100%;height:293px;float:left"></div>
		</div>
		<div class="sysNotice col">
			<blockquote class="layui-elem-quote title">客户分析</blockquote>
			<div id="show_online" style="width:50%;height:293px;float:left"></div>
			<div id="show_online_type" style="width:50%;height:293px;float:left"></div>
		</div>
	</div>
	
</body>
</html>