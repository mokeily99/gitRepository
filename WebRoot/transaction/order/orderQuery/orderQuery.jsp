<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>派发工单查询</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/order/orderQuery/orderQuery.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">手机号码</label>
					<div class="layui-input-inline">
						<input type="text" id="conn_phone" name="conn_phone" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">客户姓名</label>
					<div class="layui-input-inline">
						<input type="text" id="cust_name" name="cust_name" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">工单状态</label>
					<div class="layui-input-inline">
					      <select name="order_status" id="order_status" lay-verify="required">
				          		<option value="">请选择</option>
						        <option value="0">未完结</option>
						        <option value="1">已完结</option>
					      </select>
				    </div>
			  	</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_order_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="order_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="locus_order_div"  style="display:none;padding:20px;">
	<!-- <ul class="layui-timeline">
	  <li class="layui-timeline-item">
	    <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
	    <div class="layui-timeline-content layui-text">
	      <h3 class="layui-timeline-title">8月18日</h3>
	      <p style="color:cadetblue;">
	        layui 2.0 的一切准备工作似乎都已到位。发布之弦，一触即发。
	        <br>不枉近百个日日夜夜与之为伴。因小而大，因弱而强。
	        <br>无论它能走多远，抑或如何支撑？至少我曾倾注全心，无怨无悔 <i class="layui-icon"></i>
	      </p>
	    </div>
	  </li>
	</ul> -->
</div>
