layui.config({
	base : "js/"
}).use(['form','element','layer','jquery'],function(){
	var form = layui.form(),
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		element = layui.element(),
		$ = layui.jquery;

	$(".panel a").on("click",function(){
		window.parent.addTab($(this));
	})

	//通话信息统计
	loadConverCount();

	//获取短信总数
	loadSMSCount();

})

//短信分析
function loadSMSCount(){
	$.ajax({
		url: webpath + "/sms/getSMSInfo.action",
		type: "post",
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				$(".sms_count span").text(resultData.SMS_NUM);
				$(".send_sms_count span").text(resultData.SEND_SMS_NUM);
				$(".un_sms_count span").text(resultData.UN_SMS_NUM);
			}
		}
	});
}
//通话分析
function loadConverCount(){
	$.ajax({
		url: webpath + "/conver/getConverInfo.action",
		type: "post",
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				$(".conver_num span").text(resultData.CONVER_NUM);
				$(".into_conver_num span").text(resultData.INTO_CONVER_NUM);
				$(".out_conver_num span").text(resultData.OUT_CONVER_NUM);
			}
		}
	});
}

function getDateList(num){
	var dateList = [];
	for(var ix=num-1; ix>=0; ix--){
		var now = new Date();
	    now.setDate(now.getDate() - ix);
	    var temp = formatDate(now);
	    dateList.push(temp);
	}
	return dateList;
}

function formatDate(date){
	var month = date.getMonth() + 1;
	var day = date.getDate();
	if(month<10){
		month = "0"+month;
	}
	if(day<10){
		day = "0"+day;
	}
	return date.getFullYear() + '-' + month + '-' + day;
}