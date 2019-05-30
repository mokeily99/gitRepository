<%@page import="com.yl.common.user.pojo.UserView"%>
<%@page import="com.yl.common.user.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>待处理工单</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/order/pendingOrder/pendingOrder.js"></script>
</head>


<body>
	<div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">手机号码</label>
					<div class="layui-input-inline">
						<input type="text" id="order_phone" name="order_phone" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label" style="width:98px;">客户姓名</label>
					<div class="layui-input-inline">
						<input type="text" id="cust_name" name="cust_name" autocomplete="off" class="layui-input">
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
				<div class="layui-btn-group">
			    	<button class="layui-btn" id="turn_order_btn">转发</button>
				    <button class="layui-btn" id="over_order_btn">完结</button>
			  	</div>
		  	</div>
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="order_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="send_order_div"  style="display:none;">
	<form lay-filter="send_order_form" class="layui-form" action="" id="send_order_form">
	  <input type="hidden" id="send_order_ids" name="send_order_ids">
	  <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">派发人员</label>
	    <div class="layui-input-block">
	      <select id="order_send_opr" name="order_send_opr" style="width:300px;"></select>
	    </div>
	  </div>
	  <div class="layui-form-item layui-form-text">
	    <label class="layui-form-label">派发备注</label>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入内容" class="layui-textarea" id="send_mark_content" name="send_mark_content" style="width:300px;"></textarea>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="send_order_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="over_order_div"  style="display:none;">
	<form lay-filter="over_order_form" class="layui-form" action="" id="over_order_form">
	  <input type="hidden" id="over_order_ids" name="over_order_ids">
	  <div class="layui-form-item layui-form-text">
	    <label class="layui-form-label">完结备注</label>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入内容" class="layui-textarea" id="over_mark_content" name="over_mark_content" style="width:300px;"></textarea>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="over_order_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>	

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