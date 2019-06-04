function senWordsAn(){
	$.ajax({
		url: webpath + "/senwords/getWordsCloud.action",
		type: 'post',
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				initWordsCloud(resultData);
			}
		}
	});
}

function initWordsCloud(data){
	Highcharts.chart('sen_words_an', {
		series: [{
			type: 'wordcloud',
			data: data
		}],
		tooltip : {
			pointFormat : '<b>{point.percentage}</b>'
		},
		title: {
			text: ''
		},
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}