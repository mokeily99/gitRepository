function loadOrderPointAn() {
	$.ajax({
		url: webpath + "/order/getOrderPointCount.action",
		type: 'post',
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				initOrderPointChart(resultData);
			}
		}
	});
	
	$.ajax({
		url: webpath + "/order/getOrderSendCount.action",
		type: 'post',
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				initOrderSendChart(resultData);
			}
		}
	});
}

function initOrderPointChart(data){
	Highcharts.chart('order_type_point_an', {
		chart : {
			plotBackgroundColor : null,
			plotBorderWidth : null,
			plotShadow : false,
			type : 'pie'
		},
		title : {
			text : ''
		},
		tooltip : {
			pointFormat : '<b>{point.percentage:.1f}%</b>'
		},
		plotOptions : {
			pie : {
				allowPointSelect : true,
				cursor : 'pointer',
				dataLabels : {
					enabled : false
				},
				showInLegend : true
			}
		},
		series : [ {
			colorByPoint : true,
			data : data
		} ],
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}

function initOrderSendChart(data){
	Highcharts.chart('order_type_send_an', {
		chart : {
			plotBackgroundColor : null,
			plotBorderWidth : null,
			plotShadow : false,
			type : 'pie'
		},
		title : {
			text : ''
		},
		tooltip : {
			pointFormat : '<b>{point.percentage:.1f}%</b>'
		},
		plotOptions : {
			pie : {
				allowPointSelect : true,
				cursor : 'pointer',
				dataLabels : {
					enabled : false
				},
				showInLegend : true
			}
		},
		series : [ {
			colorByPoint : true,
			data : data
		} ],
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}