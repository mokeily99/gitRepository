<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<title>票务管理</title>
<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<link href="<%=webpath%>/common/ui/extends-layui-tree/eleTree.css" rel="stylesheet">

<script type="text/javascript" src="<%=webpath%>/transaction/system/ticketType/addticketTypes.js"></script>

<script type="text/javascript">

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
		 <fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<div style="width:99%;margin:auto;">
				<label class="layui-form-label">票务名称:</label>
				<div class="layui-inline">
					<input type="tel" id="query_ticket_name" name=“query_ticket_name” autocomplete="off" class="layui-input"  style="width:300px;"/>
				</div>
				<div class="layui-btn-group" style="margin-top:0px;">
					<button class="layui-btn" id="query_ticket_btn" lay-submit lay-filter="query_ticket_btn_sub">查询</button>
				</div>
			</div>
		</fieldset> 
		
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			<div style="width:99%;margin:auto;">
				<div class="layui-btn-group">
					<button class="layui-btn" id="add_ticket_btn">票务添加</button>
					<button class="layui-btn" id="edit_ticket_btn">票务修改</button>
					<button class="layui-btn" id="del_ticket_btn">票务删除</button>
				</div>
			</div>
			<div class="content1" style="margin-top:10px;">
				<div class="eleTree ticket_elem"></div>
			</div>
		</fieldset>
	</div>
</body>
</html>

<div id="add_ticket_div"  style="display:none;">
	<form lay-filter="add_ticket_form" class="layui-form" action="" id="add_ticket_form">
	  <input type="hidden" id="add_ticket_pid" name="add_ticket_pid">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">票务名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="add_ticket_name" id="add_ticket_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="add_ticket_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" id="cancelBtn" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>

<div id="edit_ticket_div"  style="display:none;">
	<form lay-filter="edit_ticket_form" class="layui-form" action="" id="edit_ticket_form">
	  <input type="hidden" id="edit_ticket_id" name="edit_ticket_id">
	  <div class="layui-form-item" style="margin-top:15px;">
	    <label class="layui-form-label">票务名称</label>
	    <div class="layui-input-block">
	      <input type="text" name="edit_ticket_name" id="edit_ticket_name" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:300px;">
	    </div>
	  </div>
      <div class="layui-form-item">
		<div class="layui-input-block" style="float:right;margin-right:20px;margin-top:15px;">
		  <a class="layui-btn" lay-submit lay-filter="edit_ticket_form_sub">提交</a>
		  <a class="layui-btn layui-btn-danger" href="javascript:closeDialog()">取消</a>
		</div>
	  </div>
	</form>
</div>