$(document).ready(function(){
	
	$("#registerForm").validate({
		rules: {
			username: {
				required: true,
				maxlength: 255
			},
			password: {
				required: true,
				minlength: 8,
				maxlength: 255
			},
			retype: {
				required: true,
				equalTo: "#password"
			},
			firstName: {
				required: true,
				maxlength: 255
			},
			lastName: {
				required: true,
				maxlength: 255
			},
			email: {
				required: true,
				email: true,
				maxlength: 255
			}
		},
		messages: {
			username: {
				required: Messages('validate.register.username'),
				maxlength: Messages('validate.register.username.maxlength')
			},
			password: {
				required: Messages('validate.register.password'),
				minlength: Messages('validate.register.password.minlength')
			},
			retype: {
				required: Messages('validate.register.retype'),
				equalTo: Messages('validate.register.retype.equal')
			},
			firstName: {
				required: Messages('validate.register.firstName'),
				maxlength: Messages('validate.register.firstName.maxlength')
			},			
			lastName: {
				required: Messages('validate.register.lastName'),
				maxlength: Messages('validate.register.lastName.maxlength')
			},
			email: Messages('validate.register.email'),

		}
	});
	
	$("#userType").change(function() {
		if($(this).val() === "ADMIN") {	
			$("#userType-info").tooltipster('content', Messages('info.register.admin'));
		} else if($(this).val() === "PLAYER")  {
			$("#userType-info").tooltipster('content', Messages('info.register.player'));
		}
	       
	});
	
});