$(document).ready(function(){
	
	$(".stats").hide();
	$(".achievements").hide();
	$(".important").hide();
	
	$(":button").click(function() {
		slideHideAllVisible();
		var btnClicked = "." + $(this).attr('id').split("-")[1];
		$(btnClicked).slideDown(500);
	});
	
});

function slideHideAllVisible() {
	$('.elements').children('div').each(function () {
	    if ($(this).is(":visible")) {
	    	$(this).slideUp(500);
	    }
	});
}