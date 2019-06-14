var dialogIndex;
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
	laydate.render({
	    elem: '#begin_query_date', //指定元素
	    value: year+"-"+month+"-"+date,
	    min: year+"-"+month+"-"+date
	});
	endDate = addDate(year+"-"+month+"-"+date, 20);
	laydate.render({
	    elem: '#end_query_date', //指定元素
	    value: endDate,
	    min: year+"-"+month+"-"+date
	});
	
	var blackTable = table.render({
		id: "conver_an_grid_list",
		elem : '#conver_an_grid_list',
		url : webpath + "/conver/converIsTalkAnalyse.action",
		method: "post",
		where: {beginDate: $("#begin_query_date").val(), endDate: $("#end_query_date").val()},
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
				title : '接通数'
			},
			{
				field : 'UN_TALK_NUM',
				title : '未接通数'
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_black_btn').click(function() {
		table.reload("conver_an_grid_list", {where: {beginDate: $("#begin_query_date").val(), endDate: $("#end_query_date").val()}});
	});
	
	//监听指定开关
	form.on('switch(able_switch)', function(data){
		var maxaccept = data.value;
		var ableFlag = this.checked;
		if(ableFlag){
			ableFlag = "10101";
		}else{
			ableFlag = "10102";
		}
		$.ajax({
			url: webpath + "/sms/changeBlackStatus.action",
			type: "post",
			data: {maxaccept: maxaccept, ableFlag: ableFlag},
			dataType: "json",
			success: function(data){
				var resultCode = data.resultCode;
				if(resultCode == "0000"){
					layer.msg('修改成功！');
				}else{
					layer.alert('修改失败，请重新操作！', {
						icon : 2
					});
				}
				blackTable = table.reload("conver_an_grid_list");
			}
		});
	});
	
	//黑名单添加
	$('#add_black_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '黑名单新增',
			content : $('#add_black_div'),
			area : [ '500px', '300px' ]
		});

		//黑名单添加提交
		form.on('submit(add_black_form_sub)', function(data) {

			$.ajax({
				url : webpath + "/sms/addBlackList.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						blackTable = table.reload("conver_an_grid_list");
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//黑名单修改
	$('#edit_black_btn').click(function() {
		var checkData = table.checkStatus("conver_an_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		
		if(checkData.data.length > 1){
			layer.msg('每次只能修改一条信息！',{icon:0});
			return;
		}
		
		var editeData = checkData.data[0];
		//修改赋值 
		form.val("edit_black_form", {
			  "edit_black_id": editeData.MAXACCEPT,
			  "edit_black_phone": editeData.BLACK_PHONE,
			  "edit_black_reason": editeData.BLACK_REASON
		});
		
		dialogIndex = layer.open({
			type : 1,
			title : '黑名单修改',
			content : $('#edit_black_div'),
			area : [ '500px', '300px' ]
		});

		//黑名单修改提交
		form.on('submit(edit_black_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/sms/editBlackList.action",
				method : 'post',
				data : editData.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('修改成功！');
						blackTable = table.reload("conver_an_grid_list");
					} else {
						layer.alert('修改失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//删除绑定
	$('#del_black_btn').click(function() {
		var checkData = table.checkStatus("conver_an_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		var delData = checkData.data;
		var ids = "";
		for(var ix=0; ix<delData.length; ix++){
			ids = delData[ix].MAXACCEPT + "," + ids;
		}
		
		layer.msg('确定删除选择的数据？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				
				$.ajax({
					url : webpath + "/sms/delBlackList.action",
					type : "post",
					dataType : "json",
					data : {
						ids : ids
					},
					success : function(data) {
						var resultCode = data.resultCode;
						if(resultCode == "0000"){
							layer.msg("删除成功！");
						}else{
							layer.msg('删除失败！', {icon: 5});
						}
						blackTable = table.reload("conver_an_grid_list");
					}
				});
			}
		}) ;
	});
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

