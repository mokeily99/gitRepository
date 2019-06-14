

function getConverTalkData(){
	$.ajax({
		url: webpath + "/conver/getConverIsTalkData.action",
		type: "post",
		dataType: "json",
		data: {beginDate: beginDate, endDate: endDate},
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				initConverTalkPie(resultData, "通话量占比");
			}
		}
	});
}

function initConverTalkPie(resultData, title){
	Highcharts.chart('conver_talk_an', {
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			type: 'pie'
		},
		title: {
			text: title
		},
		tooltip: {
			pointFormat: '<b>{point.percentage:.1f}%</b>'
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					format: '<b>{point.name}</b>: {point.percentage:.1f} %',
					style: {
						color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					}
				}
			}
		},
		series: [{
			colorByPoint: true,
			data: resultData
		}],
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}