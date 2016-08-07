package security;

import com.google.inject.Inject;

import models.User;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;
import services.model.UserService;
import session.Session;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;

/**
 * Deadbolt class that handles which type of user can access and activate
 * certain controller method.
 * 
 * @author Luka Ruklic
 *
 */

public class SimpleDeadboltHandler extends AbstractDeadboltHandler {

	@Inject
	protected static UserService userService;
	
	@Override
	public Promise<SimpleResult> beforeAuthCheck(Context context) {
		return null;
	}
	
	/**
	 * Method that is called on every method where some form of authentication is needed (methods with deadbolt Restrict 
	 * annotation). This method fetches user by it's session credentials. 
	 */
	@Override
	public Subject getSubject(Context context) {
		
		Subject subject = null;
		try {
			subject = JPA.withTransaction(new F.Function0<Subject>() {
				@Override
				public Subject apply() throws Throwable {
					User user = userService.findByUsername(Session.getUsername());
					return user;
				}
			});
		} catch (Throwable t) {
		}
		
		return subject;
	}
	
	/**
	 * Method that is called if authentication fails.
	 */
	@Override
	public Promise<SimpleResult> onAuthFailure(Context context, String content) {
		
        return F.Promise.promise(new F.Function0<SimpleResult>()
        {
            @Override
            public SimpleResult apply() throws Throwable {
            	// TODO what happens when authentication failed
                return unauthorized("Unauthorized");
            }
        });
	}

}
