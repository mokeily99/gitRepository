//通话分析
function getConverTalkData() {
	var bDate = $("#begin_query_date").val();
	var eDate = $("#end_query_date").val();
	if (isEmpty(bDate)) {
		bDate = beginDate;
	}
	if (isEmpty(eDate)) {
		eDate = endDate;
	}

	$.ajax({
		url : webpath + "/conver/getConverIsTalkData.action",
		type : "post",
		dataType : "json",
		data : {
			beginDate : bDate,
			endDate : eDate
		},
		success : function(data) {
			var resultCode = data.resultCode;
			if (resultCode == "0000") {
				var resultData = data.resultData;
				initConverTalkLine(resultData, "通话量占比");
			}
		}
	});
}

//通话分析
function getConverDeptTalkData() {
	var bDate = $("#begin_query_date").val();
	var eDate = $("#end_query_date").val();
	if (isEmpty(bDate)) {
		bDate = beginDate;
	}
	if (isEmpty(eDate)) {
		eDate = endDate;
	}

	$.ajax({
		url : webpath + "/conver/getConverDeptTalkData.action",
		type : "post",
		dataType : "json",
		data : {
			beginDate : bDate,
			endDate : eDate
		},
		success : function(data) {
			var resultCode = data.resultCode;
			if (resultCode == "0000") {
				var resultData = data.resultData;
				initConverTalkLine(resultData, "通话量分析");
			}
		}
	});
}

function initConverTalkLine(resultData, title) {
	var chart = Highcharts.chart('conver_talk_an', {
		chart : {
			type : 'line'
		},
		title : {
			text : title
		},
		xAxis : {
			categories : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
					'九月', '十月', '十一月', '十二月' ]
		},
		yAxis : {
			title : {
				text : '气温 (°C)'
			}
		},
		plotOptions : {
			line : {
				dataLabels : {
					// 开启数据标签
					enabled : true
				},
				// 关闭鼠标跟踪，对应的提示框、点击事件会失效
				enableMouseTracking : false
			}
		},
		series : [
				{
					name : '东京',
					data : [ 7.0, 6.9, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3,
							18.3, 13.9, 9.6 ]
				},
				{
					name : '伦敦',
					data : [ 3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2,
							10.3, 6.6, 4.8 ]
				} ]
	});
}