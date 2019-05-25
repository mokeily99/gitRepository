var dialogIndex;
layui.use(['form', 'layer', 'table', 'laydate' ], function() {
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	var laydate = layui.laydate;
	
	var converTable = table.render({
		id: "conver_grid_list",
		elem : '#conver_grid_list',
		url : webpath + "/conver/getConverList.action",
		method: "post",
		where: {callerPhone: $("#caller_phone").val(), calledPhone: $("#called_phone").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'CALLER_NO',
				title : '主叫号码'
			},
			{
				field : 'CALLED_NO',
				title : '被叫号码'
			},
			{
				field : 'CALL_TIME',
				title : '呼叫时间'
			},{
				field : 'ANSWER_TIME',
				title : '应答时间'
			},{
				field : 'HANGUP_TIME',
				title : '挂机时间'
			},{
				field : 'TALK_TIME',
				title : '通话时长(s)'
			},{
				field : 'TALK_FLAG',
				title : '通话状态',
				templet: function(row){
					if(row.TALK_FLAG == "1"){
						return "已接通";
					}else{
						return "未接通";
					}
				}
			},
			{
				field : 'HANGUP_TAR',
				title : '挂机方',
				templet: function(row){
					if(row.HANGUP_TAR == "1"){
						return "被叫挂机";
					}else{
						return "主叫挂机";
					}
				}
			},
			{
				field : 'CALL_FORWARD',
				title : '呼叫方向',
				templet: function(row){
					if(row.CALL_FORWARD == "1"){
						return "呼出";
					}else{
						return "呼入";
					}
				}
			},
			{
				field : 'SEAT_NAME',
				title : '坐席'
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_conver_btn').click(function() {
		table.reload("conver_grid_list", {where: {callerPhone: $("#caller_phone").val(), calledPhone: $("#called_phone").val()}});
	});
	
	//短信发送
	$('#send_sms_btn').click(function() {
		var checkData = table.checkStatus("conver_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		var sendData = checkData.data;
		var sendPhones = "";
		for(var ix=0; ix<sendData.length; ix++){
			var callForward = sendData[ix].CALL_FORWARD;
			
			var sendPhone;
			if(callForward == "0"){
				sendPhone = sendData[ix].CALLER_NO;
			}else{
				sendPhone = sendData[ix].CALLED_NO;
			}
			
			if(!(/^1[3456789]\d{9}$/.test(sendPhone))){ 
		        layer.msg('手机号码格式错误，不能发送短信！',{icon:0});
		        return; 
		    } 
			
			if(blackQuery(sendPhone)){
				layer.msg('黑名单客户不能发送短信！',{icon:0});
				return;
			}
			
			sendPhones = sendPhone + "," + sendPhones;
		}
		
		//号码赋值
		$("#send_phones").val(sendPhones);
		
		dialogIndex = layer.open({
			type : 1,
			title : '短信发送',
			content : $('#send_sms_div'),
			area : [ '500px', '400px' ]
		});
		
		//发送方式赋值
		LayerSelect.initLayerSelect({
			dom : "send_sms_type",
			url : webpath + "/code/getCommonCode.action",
			type : "post",
			dataType : "json",
			queryParams: {codeKey: "SEND_TYPE"},
			text : "CODE_NAME",
			id : "CODE_ID",
			defaultText: "请选择"
		});
		
		//短信模板赋值
		LayerSelect.initLayerSelect({
			dom : "sms_mould_type",
			url : webpath + "/sms/getMouldInfo.action",
			type : "post",
			dataType : "json",
			queryParams: {codeKey: "MOULD_TYPE"},
			text : "MOULD_TITLE",
			id : "MAXACCEPT",
			defaultText: "请选择"
		});
		
		//日期加载
		var myDate = new Date();
		var year = myDate.getFullYear();
		var month = myDate.getMonth()+1;
		var date = myDate.getDate();
		laydate.render({
		    elem: '#send_sms_date', //指定元素
		    value: year+"-"+month+"-"+date,
		    min: year+"-"+month+"-"+date
		});
		form.render();
		
		//下拉监听
		form.on('select(send_type)', function(data){
			var sendType = data.value;
			if(sendType == "10502"){
				$("#send_sms_date_div").show();
			}else{
				$("#send_sms_date_div").hide();
			}
		});
		form.on('select(sms_mould)', function(data){
			var maxaccept = data.value;
			$.ajax({
				url: webpath + "/sms/getMouldInfo.action",
				type:"post",
				data: {maxaccept: maxaccept},
				dataType: "json",
				success: function(data){
					if(isEmpty(maxaccept)){
						$("#send_sms_content").text("");
					}else{
						var mould = data[0];
						$("#send_sms_content").text(mould.MOULD_CONTENT);
					}
				}
			});
		});
		
		
		//短信发送提交
		form.on('submit(send_sms_form_sub)', function(data) {
			
			//判断敏感词
			var smsContent = $("#send_sms_content").val();
			smsContent = smsContent.replace(/ /g,"");
			smsContent = smsContent.replace(/\n/g,"");
			var senList = getSenWords();
			for(var ix=0; ix<senList.length; ix++){
				if(smsContent.indexOf(senList[ix].SENSITIVE_WORDS) != -1 ){
					layer.msg('短信内容包含敏感词:' + senList[ix].SENSITIVE_WORDS,{icon:0});
					return;
				}
			}

			$.ajax({
				url : webpath + "/sms/addSendSms.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						converTable = table.reload("conver_grid_list");
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

function blackQuery(phone){
	var flag = false;
	$.ajax({
		url: webpath + "/sms/getBlackInfo.action",
		type: "post",
		data: {blackPhone: phone},
		dataType: "json",
		async: false,
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				if(!isEmpty(resultData)){
					flag = true; 
				}
			}
		}
	});
	return flag;
}

function getSenWords(){
	var obj;
	$.ajax({
		url: webpath + "/senwords/getSenwordsInfo.action",
		type: "post",
		dataType: "json",
		async: false,
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				obj = data.resultData;
			}
		}
	});
	return obj;
}