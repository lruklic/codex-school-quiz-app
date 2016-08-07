package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import models.Admin;
import models.Image;
import models.Question;
import models.User;

import org.apache.commons.io.FilenameUtils;

import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.PortService;
import services.image.ImageUploader;
import services.impl.PortServiceImpl;
import services.model.QuestionService;
import services.model.UserService;
import session.Session;
import views.html.admin.admin_question;
import views.html.admin.admin_questionlist;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import cache.models.ModelCache;

import com.google.inject.Inject;

import forms.QuestionForm;

/**
 * <p>Controller that handles operations over questions.</p>
 * 
 * <p>Question operations that this controller handles are following: 
 * submitting new, editing, deleting, exporting </p>
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class QuestionController extends Controller {

	@Inject
	public static UserService userService;
	
	@Inject
	public static QuestionService questionService;
	
	@Inject
	public static ImageUploader imageUploader;
	
	@Inject
	public static PortService portService;
	
	/**
	 * Mapped to POST method under adminQuestion
	 * @return
	 */
	
	public static Result submit() {
		// Fetch question data from form
		Form<QuestionForm> questionForm = Form.form(QuestionForm.class).bindFromRequest();
		
		String username = Session.getUsername();
		User user = userService.findByUsernameOrEmail(username);
		
		// Validation
		if(questionForm.hasErrors()) {
			return badRequest(admin_question.render(questionForm, ((Admin)user).subjectPermissions, ModelCache.getInstance().getAllChapters(), ModelCache.getInstance().getAllGrades()));	// TODO check for admin twice? refactor!
		}
		
		// Check whether submit is create or edit; look for id
		boolean isEdit = false;
		try {
			Long.parseLong(questionForm.get().id);
			isEdit = true;
		} catch (NumberFormatException ex){
			// Not an edit
		}
			
		
		// Create question
		Question question = null;
		if (user instanceof Admin) {
			 question = questionForm.get().createQuestion();	// if there are any errors, .get() will throw IllegalStateException: no value
			 // question.lastEdited = System.currentTimeMillis();
		}
		
		// Get uploaded picture
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("image");
		
		if (picture != null) {
			
			// If the old picture is being replaced, delete it from file server
			if (isEdit) {
				Question oldQuestion = questionService.findById(Long.parseLong(questionForm.get().id));
				if (oldQuestion.image != null) {
					imageUploader.deleteImage(oldQuestion.subject.name, oldQuestion.image.filePath);
				}
			}
			
		    String fileName = picture.getFilename();
		    
		    File file = picture.getFile();
		    
		    long pictureId = System.currentTimeMillis();	// TODO is current time random enough for pictureId
		    
		    String newFileName = pictureId + "." + FilenameUtils.getExtension(fileName);	// add subject for better hashing
		    
		    imageUploader.uploadImage(question.subject.name, file, newFileName);
		    
		    Image image = new Image(newFileName);
		    
		    BufferedImage bimg;
			try {
				bimg = ImageIO.read(file);
				image.height = bimg.getHeight();
				image.width = bimg.getWidth();
			} catch (IOException e) {
				
			}
			
			question.image = image;
		    
		} else {
			// If submit is edit and no picture is uploaded, leave the old picture
			if (isEdit) {
				Question oldQuestion = questionService.findById(Long.parseLong(questionForm.get().id));
				question.image = oldQuestion.image;
			}
		}
		
		if (isEdit) {
			Question oldQuestion = questionService.findById(Long.parseLong(questionForm.get().id));
			if (!oldQuestion.questionType.equals(question.questionType)) {
				// If question is edited and question type has changed, delete old and create new question with same id - Hibernate can't handle this
				questionService.delete(oldQuestion);
			}
			
			question.id = oldQuestion.id;
			question.created = oldQuestion.created;
			questionService.save(question);
				
			return redirect(routes.AdminController.adminList());
			
		}
		
		questionService.save(question);

		return redirect(routes.AdminController.adminList());
	}

	@Restrict(@Group("ADMIN"))
	public static Result edit(Long id) {
		
		// TODO don't allow editing for user that is not author of question
		
		Question question = questionService.findById(id);
		
		if (!question.author.username.equals(Session.getUsername())) {
			flash("error", Messages.get("error.unauthorized.edit"));
			// TODO error, not allowed to edit question
			return redirect(routes.AdminController.adminList());
		}
		
		QuestionForm qf = new QuestionForm();
		qf.fillForm(question);
		
		qf.id = String.valueOf(question.id);
		
		Form<QuestionForm> form = Form.form(QuestionForm.class).fill(qf);
		
		return ok(admin_question.render(form, question.author.subjectPermissions, ModelCache.getInstance().getAllChapters(), ModelCache.getInstance().getAllGrades()));
		
	}
	
	@Restrict(@Group("ADMIN"))
	public static Result deleteQuestion(Long id) {
		
		// TODO deadbolt or some other handler to disable attempts for non-admin users to change question values
		
		Question question = questionService.findById(id);
		
		if (question == null) {
			return ok(admin_questionlist.render(questionService.findAll()));
		}
		
		questionService.delete(question);
		
		// If question contains image, delete it from file server
		if (question.image != null) {
			imageUploader.deleteImage(question.subject.name, question.image.filePath);
		}
		
		return redirect(routes.AdminController.adminList());
		
	}
	
}
