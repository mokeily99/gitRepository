<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>

<title>票务类型</title>
<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<link rel="stylesheet" href="../../../common/ui/layui/css/layui.css" media="all">

<script type="text/javascript">
	//var webpath = '<%=webpath%>';

	layui.use('form', function() {
		var form = layui.form;

		//监听提交
		form.on('submit(formDemo)', function(data) {
			layer.msg(JSON.stringify(data.field));
			return false;
		});
	});
	
	/* function canel(obj){
		document.getElementById(obj).style.display="none";
	} */
</script>
<body>
	<div style="padding:15px;" id="add_ticket_panel">
		<form class="layui-form" action="">
			<div class="layui-form-item">
				<label class="layui-form-label" style="float:left;width:100px;">票类名称:</label>
				<div class="layui-input-block" style="float:left;width:350px;">
					<input type="text" name="add_ticket_type_name" id="add_ticket_type_name" required lay-verify="required"
						placeholder="请输入票类名称" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="float:left;width:100px;">票类编码:</label>
				<div class="layui-input-block" style="float:left;width:350px;">
					<input type="text" name="add_ticket_type_code" id="add_ticket_type_code" required lay-verify="required"
						placeholder="请输入票类编码" autocomplete="off" class="layui-input">
				</div>
			</div>
			
			<div class="layui-form-item" style="padding:15px;float:right;">
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit lay-filter="formDemo">提交</button>
					<button id="canel" onclick="javascript:canel('add_ticket_panel');" type="reset" class="layui-btn layui-btn-primary">取消</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>


