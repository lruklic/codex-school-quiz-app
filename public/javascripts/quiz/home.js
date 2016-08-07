$(document).ready(function(){
	
	// Custom multiple select for chapters
	$('.selectpicker').selectpicker();
	
	$(".subject-picker").hide();
	$(".chapter-picker").hide();
	$(".additional-picker").hide();
	$(".btn-submit").hide();
	
	
	// On grade pick, show subject
	$("#grade").change(function(){
    	$(".subject-picker").slideDown(500);
    	chapterChange();
    });
	
	$("#subject").change(function() {
		$(".chapter-picker").slideDown(500);
		$(".additional-picker").slideDown(500);
		$(".btn-submit").slideDown(500);
		chapterChange();
	});
	
});

function chapterChange() { // duplicate from questionInput, refactor
	var currentGrade = $("#grade").val();
	var currentSubject = $("#subject").val();
	var numberOfChapters = $("#chapter > option").length;
	for (int = 0; int < numberOfChapters; int++) {
		var chapter = $("#check-"+int).val();
		var chapterValues = chapter.split('-');
		if (currentGrade !== chapterValues[0] || currentSubject !== chapterValues[1]) {
			$("#check-"+int).hide();
		} else {
			$("#check-"+int).show();
		}
	}
	
	$("#chapter option:selected").removeAttr("selected");
	$('.selectpicker').selectpicker('refresh');
}