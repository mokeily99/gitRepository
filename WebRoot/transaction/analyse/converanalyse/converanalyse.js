var dialogIndex;
var beginDate;
var endDate;
layui.use(['form', 'layer', 'table', 'laydate' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	var laydate = layui.laydate;
	
	//日期加载
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth()+1;
	if(month < 10){
		month = "0"+month;
	}
	var date = myDate.getDate();
	if(date < 10){
		date = "0"+date;
	}
	beginDate = addDate(year+"-"+month+"-"+date, -10);
	laydate.render({
	    elem: '#begin_query_date', //指定元素
	    value: beginDate,
	    min: addDate(year+"-"+month+"-"+date, -30)
	});
	
	endDate = year+"-"+month+"-"+date;
	laydate.render({
	    elem: '#end_query_date', //指定元素
	    value: endDate,
	    max: endDate,
	    min: addDate(year+"-"+month+"-"+date, -30)
	});
	
	var blackTable = table.render({
		id: "conver_an_grid_list",
		elem : '#conver_an_grid_list',
		url : webpath + "/conver/converIsTalkAnalyse.action",
		method: "post",
		where: {beginDate: beginDate, endDate: endDate},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'SEAT_NAME',
				title : '坐席姓名'
			},
			{
				field : 'CONVER_NUM',
				title : '通话总量'
			},
			{
				field : 'TALK_NUM',
				title : '已接通数'
			},
			{
				field : 'UN_TALK_NUM',
				title : '未接通数'
			}
		] ],
		limit: 5,
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_black_btn').click(function() {
		table.reload("conver_an_grid_list", {where: {beginDate: $("#begin_query_date").val(), endDate: $("#end_query_date").val()}});
		getConverDeptTalkData();
	});
	
	/***************************************图形模块*************************************************/
	getConverDeptTalkData();
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

