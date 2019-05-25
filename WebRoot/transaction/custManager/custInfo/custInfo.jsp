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

<title>客户资料管理</title>

<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/custManager/custInfo/custInfo.js"></script>
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
					<label class="layui-form-label" style="width:98px;">客户姓名</label>
					<div class="layui-input-inline">
						<input type="text" id="cust_name" name="cust_name" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline" id="cust_type_div" style="display:none;">
					<label class="layui-form-label" style="width:98px;">客户类型</label>
					<div class="layui-input-inline">
						<select id="cust_type" lay-verify="required"></select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="query_cust_btn">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			
			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
			    	<button class="layui-btn" id="add_cust_btn">单个资料录入</button>
			    	<button class="layui-btn" id="bat_cust_btn">批量资料录入</button>
				    <button class="layui-btn" id="edit_cust_btn">编辑</button>
				    <button class="layui-btn" id="del_cust_btn">删除</button>
				    <button class="layui-btn" id="send_sms_btn">短信发送</button>
			  	</div>
		  	</div>
		  	<div style="width:99%;margin:auto;">
			  	<table class="layui-hide" id="cust_grid_list"></table>
		  	</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_cust_div"  style="display:none;">
	<form lay-filter="add_cust_form" class="layui-form" action="" id="add_cust_form">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">客户姓名</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入客户姓名" name="add_cust_name" id="add_cust_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">性别</label>
	    <div class="layui-input-block">
	      <select id="add_cust_sex" name="add_cust_sex" lay-verify="required"></select>
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">年龄</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入客户年龄" name="add_cust_age" id="add_cust_age" required  lay-verify="required|number" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">联系电话</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入联系电话" name="add_conn_phone" id="add_conn_phone" required  lay-verify="required|connPhone" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">地址</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入客户地址" name="add_cust_addr" id="add_cust_addr" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">客户类型</label>
	    <div class="layui-input-block">
	      <select id="add_cust_type" name="add_cust_type" style="width:300px;"></select>
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">备注</label>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入备注" class="layui-textarea" id="add_cust_mark" name="add_cust_mark" style="width:300px;"></textarea>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_cust_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="bat_cust_div" style="display:none;">
	<form lay-filter="bat_cust_form" class="layui-form" action="" id="bat_cust_form">
		<div style="width:90%;margin:auto;">
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
				<legend>客户资料文件上传</legend>
			</fieldset>
			<div class="layui-upload-drag" id="test10">
				<i class="layui-icon"></i>
				<p>点击上传，或将文件拖拽到此处</p>
			</div>
			
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
				<legend>客户资料模板下载</legend>
			</fieldset>
			<a href="<%=webpath%>/cust/downlodCustFile.action" style="color:blue;text-decoration:underline;">点击下载资料模板</a>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block"
				style="float:right;margin-right:20px;margin-top:15px;">
				<a class="layui-btn" lay-submit lay-filter="bat_cust_form_sub" id="bat_cust_form_sub">提交</a>
				<a class="layui-btn layui-btn-danger" id="cancelBtn"
					href="javascript:closeDialog()">取消</a>
			</div>
		</div>
	</form>
</div>

<div id="edit_cust_div"  style="display:none;">
	<form lay-filter="edit_cust_form" class="layui-form" action="" id="edit_cust_form">
	  <input type="hidden" id="edit_cust_id" name="edit_cust_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">客户姓名</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入客户姓名" name="edit_cust_name" id="edit_cust_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">性别</label>
	    <div class="layui-input-block">
	      <select id="edit_cust_sex" name="edit_cust_sex" lay-verify="required"></select>
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">年龄</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入客户年龄" name="edit_cust_age" id="edit_cust_age" required  lay-verify="required|number" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">联系电话</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入联系电话" name="edit_conn_phone" id="edit_conn_phone" required  lay-verify="required|connPhone" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">地址</label>
	    <div class="layui-input-block">
	      <input type="text" placeholder="请输入客户地址" name="edit_cust_addr" id="edit_cust_addr" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;width: 411px;">
	    <label class="layui-form-label">客户类型</label>
	    <div class="layui-input-block">
	      <select id="edit_cust_type" name="edit_cust_type" style="width:300px;"></select>
	    </div>
	  </div>
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">备注</label>
	    <div class="layui-input-block">
	      <textarea placeholder="请输入备注" class="layui-textarea" id="edit_cust_mark" name="edit_cust_mark" style="width:300px;"></textarea>
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_cust_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="send_sms_div" style="display:none;">
	<form lay-filter="send_sms_form" class="layui-form" action="" id="esend_sms_form">
		<input type="hidden" id="cust_names" name="cust_names">
		<input type="hidden" id="send_phones" name="send_phones">
		<div class="layui-form-item" style="margin-top:15px;width: 411px;">
			<label class="layui-form-label">发送方式</label>
			<div class="layui-input-block">
				<select id="send_sms_type" name="send_sms_type" lay-verify="required" lay-filter="send_type"></select>
			</div>
		</div>
		<div class="layui-form-item" id="send_sms_date_div" style="margin-top:15px;width: 411px;display:none;">
			<label class="layui-form-label">发送日期</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input" id="send_sms_date" name="send_sms_date">
			</div>
		</div>
		<div class="layui-form-item" style="margin-top:15px;width: 411px;">
			<label class="layui-form-label">插入模板</label>
			<div class="layui-input-block">
				<select id="sms_mould_type" name="sms_mould_type" lay-filter="sms_mould"></select>
			</div>
		</div>
		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">短信内容</label>
			<div class="layui-input-block">
				<textarea class="layui-textarea" id="send_sms_content" name="send_sms_content" lay-verify="required" style="width:300px;"></textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block"
				style="float:right;margin-right:20px;margin-top:15px;">
				<a class="layui-btn" lay-submit lay-filter="send_sms_form_sub">提交</a>
				<a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
			</div>
		</div>
	</form>
</div>