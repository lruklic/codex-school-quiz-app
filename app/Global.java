import org.h2.engine.Session;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import security.SimpleDeadboltHandler;
import services.EmailService;
import services.PortService;
import services.image.ImageUploader;
import services.image.impl.AmazonImageUploader;
import services.impl.EmailServiceImpl;
import services.impl.PortServiceImpl;
import services.model.ActivationLinkService;
import services.model.ChapterService;
import services.model.FacebookAuthService;
import services.model.GradeService;
import services.model.NoveltyService;
import services.model.QuestionService;
import services.model.SubjectService;
import services.model.UserService;
import services.model.impl.ActivationLinkServiceImpl;
import services.model.impl.ChapterServiceImpl;
import services.model.impl.FacebookAuthServiceImpl;
import services.model.impl.GradeServiceImpl;
import services.model.impl.NoveltyServiceImpl;
import services.model.impl.QuestionServiceImpl;
import services.model.impl.SubjectServiceImpl;
import services.model.impl.UserServiceImpl;
import cache.models.ModelCache;

import com.amazonaws.SDKGlobalConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import controllers.ActivationController;
import controllers.AdminController;
import controllers.LoginController;
import controllers.NewsController;
import controllers.PortController;
import controllers.ProfileController;
import controllers.QuestionController;
import controllers.QuizController;
import controllers.RegisterController;
import controllers.StartController;
import controllers.TestDataController;
import factories.QuestionFactory;


/**
 * <p>Global class that extends GlobalSettings that has onStart method which triggers when application is started.</p>
 * 
 * @author Luka Ruklic
 *
 */

public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application application) {
		
		Logger.info("App has started!");
		
		// Property for AWS
		System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
		
		Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				
				requestStaticInjection(TestDataController.class);
				requestStaticInjection(LoginController.class);
				requestStaticInjection(QuestionController.class);
				requestStaticInjection(RegisterController.class);
				requestStaticInjection(StartController.class);
				requestStaticInjection(AdminController.class);
				requestStaticInjection(NewsController.class);
				requestStaticInjection(QuizController.class);
				requestStaticInjection(ProfileController.class);
				requestStaticInjection(PortController.class);
				requestStaticInjection(ActivationController.class);
				requestStaticInjection(SimpleDeadboltHandler.class);
				requestStaticInjection(Session.class);
				requestStaticInjection(ModelCache.class);
				requestStaticInjection(QuestionFactory.class);
				requestStaticInjection(PortServiceImpl.class);
				
				bind(UserService.class).to(UserServiceImpl.class);
				bind(QuestionService.class).to(QuestionServiceImpl.class);
				bind(NoveltyService.class).to(NoveltyServiceImpl.class);
				bind(SubjectService.class).to(SubjectServiceImpl.class);
				bind(ChapterService.class).to(ChapterServiceImpl.class);
				bind(GradeService.class).to(GradeServiceImpl.class);
				bind(FacebookAuthService.class).to(FacebookAuthServiceImpl.class);
				bind(ActivationLinkService.class).to(ActivationLinkServiceImpl.class);
				bind(ImageUploader.class).to(AmazonImageUploader.class);
				bind(EmailService.class).to(EmailServiceImpl.class);
				bind(PortService.class).to(PortServiceImpl.class);
			}
			
		});
	}

}
