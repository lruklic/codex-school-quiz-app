package forms;

import models.enums.UserType;
import play.data.validation.Constraints.Required;

public class RegisterForm {
	
	@Required
	public String username;
	
	@Required
	public String password;
	
	@Required
	public String retype;
	
	@Required
	public String email;
	
	public UserType userType;
	
	@Required
	public String firstName;
	
	@Required
	public String lastName;

}
