package forms;

import play.data.validation.Constraints.Required;

public class LoginForm {
	
	@Required
	public String usernameOrEmail;
	
	@Required
	public String password;

}
