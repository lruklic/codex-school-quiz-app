$(document).ready(function(){
	
	// Hide detailed export buttons
	$(".export").hide();
	$("#import-input").hide();
	$("#import-button").hide();
	
	// Predefined method for table sorting
	$('.question-table').DataTable( {
		"searching": true,
		"order": [[ 0, "desc" ]],
		"colReorder": true,
		"stateSave": true,
		"language": {
			"url": "//cdn.datatables.net/plug-ins/1.10.7/i18n/Croatian.json"
		}
	});
	
	// DataTable
    var table = $('.question-table').DataTable();
    
	// Method call for filtering table content on keystroke
	$("#filter").on('change keyup paste mouseup', function() {
		filter(table);
	});
	
	$('#filterType').change(function(e){
		filter(table);
	});
	
	$(".export-questions").on("click", function() {
		$(".export").toggle();
	});
	
	$(".import-questions").on("click", function() {
		$("#import-input").toggle();
		$("#import-button").toggle();
	});
	
	$("#import-button").on("click", function() {
		
		var file = $('#import-input')[0].files[0];
		
		var data = new FormData();
		data.append('import-input', file);
		
		$.ajax({
			url: "/import",
			data: data,
			type: "POST",
			cache: false,
			contentType: false,
			processData: false,
			success: function(response) {
				$('.modal-table > tbody').empty();
				$('#import-modal').modal('show');
				$('.modal-table > tbody').html(generateImportTable(response));
				table.ajax.reload();
			}
		});
		
	});
	
	// Starts dialog button on delete
	$(".delete-button").click(function(ev) {
	    var link = this;

	    ev.preventDefault();
	    
	    var href = link.href;
	    var hrefArray = href.split("/");
	    var hrefLen = hrefArray.length;
	    
	    var questionId = hrefArray[hrefLen-1];
	    var trName = "question-"+questionId;
	    var questionText = $("#"+trName + " > .table-questionText").html();
	    
	    var deleteQuestion = Messages("question.delete");
	     
	    $("<div>Jeste li sigurni da želite obrisati pitanje? <br><br> Id: "+questionId+"<br> Tekst: "+questionText+"</div>").dialog({
	        buttons: [
				{
					text: Messages('question.delete'),
					click: function() {
		                jsRoutes.controllers.QuestionController.deleteQuestion(questionId).ajax({
		                	success: function(data) {
		                		location.reload();
		                	}
		                });
					}
				},
				{
					text: Messages('question.cancel'),
					click: function() {
						$(this).dialog("close");
					}
				},
			]
	        
	    });
	});
});


function generateImportTable(response) {
	
	var tableContent;
	
	$.each(JSON.parse(response), function(index, element) {
		var importResult = JSON.parse(JSON.parse(response)[index]);	
	    tableContent += "<tr><td>" + importResult['import.status'] + "</td><td>" + importResult['import.error'] + "</td></tr>";
	});
	
	return tableContent;
}

/**
 * Function that invokes DataTables search method with filter parameter
 * and re-draws the whole question table with filtered results.
 */
function filter(table) {
	
	table.columns().search("").draw();		// resets the search; 
	
	var filter = $("#filter").val();
	var searchColumn = getColumnNumber($("#filterType").val());
	
	table.column(searchColumn).search(filter).draw();
	
}

/**
 * Function that returns predefined column number in question table based on 
 * <i>filterType</i> string which defines which search filter is used.
 * 
 * @param filterType string that defines which search filter is used
 * @returns {Number} predefined number for one of the columns in question table
 */
function getColumnNumber(filterType) {
	switch(filterType) {
		case "TEXT_FILTER":
			return 1;
		case "TYPE_FILTER":
			return 2;
		case "SUBJECT_FILTER":
			return 3;
		case "CHAPTER_FILTER":
			return 4
		case "SUBMITTER_FILTER":
			return 5;
		default:
			return 0;
	}
}


