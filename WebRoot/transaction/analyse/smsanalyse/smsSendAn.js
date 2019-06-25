//通话分析
function getSMSDeptTalkData() {
	var bDate = $("#begin_query_date").val();
	var eDate = $("#end_query_date").val();
	if (isEmpty(bDate)) {
		bDate = beginDate;
	}
	if (isEmpty(eDate)) {
		eDate = endDate;
	}

	var dateList = getDateSpaceList(new Date(bDate), new Date(eDate));
	$.ajax({
		url : webpath + "/sms/getSMSDeptTalkData.action",
		type : "post",
		dataType : "json",
		data : {
			dateList: dateList
		},
		success : function(data) {
			var resultCode = data.resultCode;
			if (resultCode == "0000") {
				var resultData = data.resultData;
				initSMSLine(dateList, resultData, "短信量分析");
			}
		}
	});
}

function initSMSLine(categories, resultData, title) {
	var chart = Highcharts.chart('sms_send_an', {
		chart : {
			type : 'line'
		},
		title : {
			text : title
		},
		xAxis : {
			categories : categories
		},
		yAxis : {
			title : {
				text : '短信量'
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
		series : resultData,
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}