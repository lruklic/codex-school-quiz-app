function initAdmin() {
	
	// Custom multiple select for chapters
	$('.selectpicker').selectpicker();
	
	// Activate and customize TinyMCE WYSIWYG embedded text editor
	tinymce.init({
		selector:".rich-text",
		menubar: false,
		toolbar: "undo redo | styleselect | bold italic | link image",
		plugins: "link",
		content_css: '/assets/stylesheets/content.min.css',
		paste_postprocess : function(pl, o) {
	        // remove extra line breaks
	        o.node.innerHTML = o.node.innerHTML.replace(/&nbsp;/ig, " ");
	    }
	});
	
	// Initialize Bootstrap Switch
	$("[name='trueFalse']").bootstrapSwitch({
		onColor:'success',
		offColor:'danger',
	});
	
	$('.extra').hide();
	
	// Admin page question input initialization - starting question type is Multiple Choice
	var editId = $('[name=id]').val();
	var editChapters = $('[name=editChapters]').val();
	
	// Default number of answers is set to 3
	for (var int = 2; int < 5; int++) {
    	$('.incorrect-'+int).hide();
	}
	
	if($('#multipleNumber').val() === "") {
		$('#multipleNumber').val(3);
	} else {
		var numberOfAnswers = $('#multipleNumber').val();
		for (var int = 1; int < numberOfAnswers; int++) {
	    	$('.incorrect-'+int).show();
		}
	}
	
	// Set Question Type based on current question
	$(".hidden").removeClass("hidden");
	setQuestionType();
	
	// Initial subject
	var numberOfChapters = $("#chapter > option").length;
	
	var isEdit = false;
	if (editId !== "/") {
		isEdit = true;
	}
	
	chapterChange();
	
	// Set chapters based on current grade and subject
	for (int = 0; int < numberOfChapters; int++) {
		var chapter = $("#check-"+int).val();
		var chapterValues = chapter.split('-');
		var currentChapters = editChapters.replace(/\[|\]|/g, "").split(',');
		
		if (chapterValues[0] !== $("#grade").val() || chapterValues[1] !== $("#subject").val()) {	// why gimn_1st
			$("#check-"+int).hide();
		} else {
			$("#check-"+int).show();
		}
		
		// If question is open for editing, set selected chapters
		if (isEdit) {
			
			for (var j = 0; j < currentChapters.length; j++) {
				if (currentChapters[j].trim() === chapterValues[2]) {
					$("#check-"+int).prop('selected', true);
				}
			}	
		}
		
		$('.selectpicker').selectpicker('refresh');
			
	}
	
	// If question is edit and has special tags, show them
	if (isEdit && ($("#finalExam").is(":checked") || $("#competition").is(":checked"))) {
		$('.extra').show();
		$('#extra-trigger').prop('checked', true);
	}
	
}

function setQuestionType() {
	var qType = $("#qType").val().toLowerCase();
	
	$(".numberOfAnswers").hide();
	$(".multiple_choice").hide();
	$(".multiple_answer").hide();
	$(".true_false").hide();
	$(".input_answer").hide();
	$(".connect_correct").hide();
	$(".composed").hide();

	if (qType === "multiple_choice" || qType === "multiple_answer") {
		$(".numberOfAnswers").show();
		questionNumberChange();
	}
	
	$("."+qType).show();
	
}

function questionNumberChange() {
	if ($.isNumeric($('#multipleNumber').val()) && $('#multipleNumber').val() < 7 && $('#multipleNumber').val() > 2) {
		var numberOfAnswers = $('#multipleNumber').val();
		
		// case with Multiple Choice selected
		if ($("#qType").val() === "MULTIPLE_CHOICE") {
    		// Hide extra inputs
    		for (var int = 2; int < 5; int++) {
        		$('.incorrect-'+int).hide();
			}
    		// Dynamically show input text boxes for incorrect answers
    		for (var int = 2; int < numberOfAnswers-1; int++) {
        		$('.incorrect-'+int).show();
			}
    		
    	// case with Multiple Answer selected
		} else {
			// Hide extra inputs
    		for (var int = 0; int < 6; int++) {
        		$('.multiple-'+int).hide();
			}
    		// Dynamicly show input text boxes for answers
    		for (var int = 0; int < numberOfAnswers; int++) {
        		$('.multiple-'+int).show();
			}
			
		}

	} else {
		$('#multipleNumber').val(3);
		
		// Hide extra inputs
		for (var int = 2; int < 5; int++) {
    		$('.incorrect-'+int).hide();
		}
		for (var int = 3; int < 6; int++) {
    		$('.multiple-'+int).hide();
		}
	}
}

function chapterChange() {
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

$(document).ready(function(){
	
	initAdmin();
	
    $("#qType").change(function(){
    	setQuestionType();
    });
    
    $(".chapter-trigger").change(function(){
    	chapterChange();
    });
    
    $("#extra-trigger").change(function(){
    	$('.extra').toggle();
    });
    
    $("#finalExam").change(function(){
    	if($("#competition").prop('checked')) {
    		$("#competition").prop('checked', false);
    	}
    });
    
    $("#competition").change(function(){
    	if($("#finalExam").prop('checked')) {
    		$("#finalExam").prop('checked', false);
    	}
    });
    
//    $("#chapter").change(function(){
//    	var t1 = $.now();
//    	$.ajax({
//    		url: '/question/chapters/GIMN_1ST/HISTORY',
//    		success: function(data) {
//    			data;
//    			var t2 = $.now();
//    			alert(t2 - t1);
//    		},
//    		type: 'GET'
//    	});
//    });
    
    // Method that creates or deletes incorrect answer input forms when number of answers is changed
    $('#multipleNumber').on('input',function(){
    	questionNumberChange();
    });
}); 