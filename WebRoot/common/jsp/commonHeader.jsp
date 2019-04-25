<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webpath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache" />

<!--[if lt IE 9]>
<meta http-equiv="refresh" content="0;ie.html" />
<![endif]-->

<link rel="shortcut icon" href="<%=webpath%>/common/ui/hAdmin/img/favicon.ico" type="image/x-icon"> 
<link href="<%=webpath%>/common/ui/bootstrap3.3.7/docs/assets/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="<%=webpath%>/common/ui/bootstrap3.3.7/dist/bootstrap-table.css" rel="stylesheet">
<link href="<%=webpath%>/common/ui/hAdmin/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
<link href="<%=webpath%>/common/ui/hAdmin/css/animate.css" rel="stylesheet">
<link href="<%=webpath%>/common/ui/hAdmin/css/style.css?v=4.1.0" rel="stylesheet">
<link href="<%=webpath%>/common/ui/bootstrap3.3.7/column/bootstrap-table-fixed-columns.css" rel="stylesheet">


<!-- 全局js -->
<script src="<%=webpath%>/common/ui/hAdmin/js/jquery.min.js?v=2.1.4"></script>
<script src="<%=webpath%>/common/ui/bootstrap3.3.7/docs/assets/bootstrap/js/bootstrap.js"></script>
<script src="<%=webpath%>/common/ui/bootstrap3.3.7/dist/bootstrap-table.js"></script>
<script src="<%=webpath%>/common/ui/bootstrap3.3.7/dist/locale/bootstrap-table-zh-CN.js"></script>
<script src="<%=webpath%>/common/ui/bootstrap3.3.7/column/bootstrap-table-fixed-columns.js"></script>
<script src="<%=webpath%>/common/ui/hAdmin/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="<%=webpath%>/common/ui/hAdmin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="<%=webpath%>/common/ui/hAdmin/js/plugins/layer/layer.min.js"></script>

<style type="text/css">
	body{
		background: #f2f3f8;
		box-shadow:8px 0px 20px #d7d7d7 inset;
		font-size:12px;
	}
	.page_panel {
		width: 100%;
		height:100%;
	}
	.page-header {
	    padding: 20px 0 9px;
	    margin: 0 0 20px;
	    border-bottom: 1px solid #e0dede;
	}
	.content_div{
		background: #ffffff;
		padding:10px 20px 10px 20px;
		width:50%;
		box-shadow: 3px 0px 20px #d7d7d7
	}
	.personnel_table_list{
		font-size:13px;
	}
	
	.btn-group .dropdown-toggle{
		padding: 0px 5px;
	}
</style>
<script type="text/javascript">
	$(function(){
		//自动加载表格高度
		auto_table_H();
		$(window).resize(function () {
			setTimeout("location.reload();",100);
		});
	});
	var H = "";
	function auto_table_H(){
		var windowH = $(window).height();
		var topH = $(".page-header").height();
		H = windowH - topH - 100;
	}
</script>