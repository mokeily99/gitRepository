$(function() {
	// ×ÔÊÊÓ¦ÆÁÄ»¿í¶È
	window.onresize = function() {
		location = location
	};

	var main_h = $(window).height();
	$('.hy_list').css('height', main_h - 45 + 'px');

	var search_w = $(window).width() - 40;
	$('.search').css('width', search_w + 'px');
	// $('.list_hy').css('width',search_w+'px');
	
});