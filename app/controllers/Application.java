package controllers;

import jsmessages.JsMessages;
import play.Play;
import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	final static JsMessages messages = JsMessages.create(Play.application());
	
	public static Result javascriptRoutes() {
	    response().setContentType("text/javascript");
	    return ok(
	        Routes.javascriptRouter("jsRoutes",
	            routes.javascript.QuestionController.deleteQuestion(),
	            routes.javascript.PortController.importQuestions(),
	            routes.javascript.QuizController.quizHome()
	            //inside somepackage
	            // controllers.somepackage.routes.javascript.Application.updateItem()
	        )
	    );
	}
	
	public static Result jsMessages() {
		return ok(messages.generate("window.Messages"));
	}
	
}
