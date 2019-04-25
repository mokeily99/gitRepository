<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
 String webpath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    
<title>票务类型</title>
<jsp:include page="../../../common/jsp/commonContent.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/transaction/system/ticketType/ticketType.js"></script>
<link rel="stylesheet" href="../../../common/ui/layui/css/layui.css"  media="all">
	
<script type="text/javascript">
	var webpath = '<%=webpath%>';
	
	layui.use('layer', function(){ //独立版的layer无需执行这一句
		//触发事件
		var active = {
		  offset: function(othis){
		    var type = othis.data('type') ,text = othis.text();
		    
		    layer.open({
		      type: 2
		      ,offset: '100px' //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
		      ,id: 'layerDemo'+type //防止重复弹出
		      ,title: ['票类新增', 'font-size:18px;']
		      //,content: [webpath+'/transaction/system/ticketType/add.jsp', 'no']
		      ,content: webpath+'/transaction/system/ticketType/add.jsp'
		      //,btn: ['提交','取消']
		      ,btnAlign: 'c' //按钮居中
		      ,shade: 0 //不显示遮罩
		      ,area: ['600px', '240px']
		      /* ,btn1: function(){
			      alert("yes");
		          layer.closeAll();
			  }
		      ,btn2:function(){
		      	  alert("no");
		      } */
		    });
		  }
		};
	  
		$('#add_ticket').on('click', function(){
		  var othis = $(this), method = othis.data('method');
		  active[method] ? active[method].call(this, othis) : '';
		});
	  
	});
</script>
  
<body>
    <div class="page_panel">
		<fieldset class="layui-elem-field site-demo-button query-fieldset" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>查询区域</legend>
			<form class="layui-form" action="">
				<div class="layui-inline">
					<label class="layui-form-label">票务名称</label>
					<div class="layui-input-inline">
						<input type="tel" id="user_name" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label" id="queryConditions"></label>
					<div class="layui-input-inline">
						<button type="button" class="layui-btn layui-btn-normal" id="add_ticket_query" onclick="loadTicketData();">查询</button>
						<button type="reset" class="layui-btn layui-btn-danger">重置</button>
					</div>
				</div>
			</form>
		</fieldset>
		<fieldset class="layui-elem-field site-demo-button" style="margin: 30px;padding-top:20px;padding-bottom:20px;">
			<legend>工作区域</legend>
			
		  	<div style="width:99%;margin:auto;">
			  	<div class="layui-btn-group" id="layerDemo" style="margin-bottom: 0;">
					<button data-method="offset" data-type="auto" class="layui-btn" id="add_ticket">增加</button>
					<button data-method="offset" data-type="auto" class="layui-btn" id="">编辑</button>
					<button data-method="offset" data-type="auto" class="layui-btn" id="">删除</button>
				</div>
			</div>
		  	<div id="ticket_table_list" class="ticket_table_list" style="white-space:nowrap;width:99%;margin:auto;margin-top:10px;"></div>
		</fieldset>
	</div>
</body>
</html>
<%-- <div class="modal fade" id="add_ticket_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<jsp:include page="add.jsp"></jsp:include>
</div> --%>
<%-- <div class="modal fade" id="mod_dept_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<jsp:include page="mod.jsp"></jsp:include>
</div> --%>