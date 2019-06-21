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

<link href="<%=webpath%>/common/ui/treeselect/assets/layui/css/layui.css" rel="stylesheet">

<script type="text/javascript" src="<%=webpath%>/common/js/jquery-1.10.2.min.js"></script>
<script src="<%=webpath%>/common/ui/treeselect/assets/layui/layui.js"></script>
<script type="text/javascript" src="<%=webpath%>/common/ui/layui/layui.extends.js"></script>

<script src="<%=webpath%>/common/js/Highcharts-7.1.1/code/highcharts.js"></script>
<script src="<%=webpath%>/common/js/Highcharts-7.1.1/code/modules/exporting.js"></script>
<script src="<%=webpath%>/common/js/Highcharts-7.1.1/code/modules/wordcloud.js"></script>
<script src="<%=webpath%>/common/js/Highcharts-7.1.1/code/highcharts-zh_CN.js"></script>
	
<script type="text/javascript">
	var webpath = '<%=webpath%>';
	$(function() {
		//表单组件渲染
		layui.use('form', function() {
			var form = layui.form;
			form.render();
		});
	});
	
	/**************公共校验是否为空方法begin**************/
	function isEmpty(val){
		if(val == null || val == undefined || val == ""){
			return true;
		}else{
			return false;
		}
	}
	/**************公共校验是否为空方法begin**************/
	
	/**************公共无效变量替换方法begin**************/
	function nvl(val, newVal){
		if(val == null || val == undefined || val == ""){
			return newVal;
		}else{
			return val;
		}
	}
	/**************公共无效变量替换方法begin**************/
	
	/**************公共编码转换方法begin**************/
	function getCodeName(codeKey, codeID){
		var obj = "";
		$.ajax({
			url: webpath + "/code/getCommonCode.action",
			type: "post",
			async: false,
			data: {codeKey: codeKey, codeIDS: codeID},
			success: function(data){
				obj = eval("(" + data + ")");
			}
		});
		return obj;
	}
	/**************公共编码转换方法end **************/
	
	/**************公共客户类型编码转换方法begin**************/
	function getCustTypeCodeName(codeKey, codeID){
		var obj = "";
		$.ajax({
			url: webpath + "/code/getCustType.action",
			type: "post",
			async: false,
			data: {codeKey: codeKey, codeIDS: codeID},
			success: function(data){
				obj = eval("(" + data + ")");
			}
		});
		return obj;
	}
	/**************公共客户类型编码转换方法end **************/
	
	/************日期加减begin************/
	function addDate(date,days){
      	var d=new Date(date); 
       	d.setDate(d.getDate()+days); 
       	var m=d.getMonth()+1; 
       	if(m < 10){
       		m = "0"+m;
       	}
       	var day = d.getDate();
       	if(day < 10){
			day = "0"+day;
		}
       	return d.getFullYear()+'-'+m+'-'+day; 
    } 
	/************日期加减end************/
	
	/************日期间隔************/
	function getDateSpaceList(beginDate, endDate){
		var date = (endDate.getTime()-beginDate.getTime())/(1000*60*60*24)+1;
		var dateList = [];
		for(var ix=0; ix<date; ix++){
			var temp = addDate(beginDate, ix)
		    dateList.push(temp);
		}
		return dateList;
	}
	/************日期间隔************/
	
	/************初始化表格高度begin************/
	var H = "";
	function auto_table_H(){
		var windowH = $(window).height();
		var topH = $(".page-header").height();
		H = windowH - topH - 150;
	}
	/************初始化表格高度end************/
	
	/************图片转base64 begin************/
	function getBase64(imgSrc, dom) {//传入图片路径，返回base64
		if(isEmpty(imgSrc)){
			$("#"+dom).val(imgSrc);
		}else{
			var image = new Image();
			image.src = imgSrc;
			if (image) {
				var canvas = document.createElement("canvas");
				var ctx = canvas.getContext("2d");
				image.onload = function (){
					ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
					var dataURL = canvas.toDataURL();
					$("#"+dom).val(dataURL);
				};
			}
		}
	}
	/************图片转base64 end************/
	
	$(document).on("click", ".layui-table-body table.layui-table tbody tr", function (e) {
	    if ($(e.target).hasClass("layui-table-col-special") || $(e.target).parent().hasClass("layui-table-col-special")) {
	        return false;
	    }
	    var index = $(this).attr('data-index'), tableBox = $(this).closest('.layui-table-box'),
	        tableFixed = tableBox.find(".layui-table-fixed.layui-table-fixed-l"),
	        tableBody = tableBox.find(".layui-table-body.layui-table-main"),
	        tableDiv = tableFixed.length ? tableFixed : tableBody,
	        checkCell = tableDiv.find("tr[data-index=" + index + "]").find("td div.laytable-cell-checkbox div.layui-form-checkbox i"),
	        radioCell = tableDiv.find("tr[data-index=" + index + "]").find("td div.laytable-cell-radio div.layui-form-radio i");
	    if (checkCell.length) {
	        checkCell.click();
	    }
	    if (radioCell.length) {
	        radioCell.click();
	    }
	});
	$(document).on("click", "td div.laytable-cell-checkbox div.layui-form-checkbox,td div.laytable-cell-radio div.layui-form-radio", function (e) {
	    e.stopPropagation();
	});
</script>