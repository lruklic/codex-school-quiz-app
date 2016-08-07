package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Admin;
import models.Chapter;
import models.Grade;
import models.Player;
import models.Subject;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.PasswordHash;
import services.model.ChapterService;
import services.model.GradeService;
import services.model.NoveltyService;
import services.model.SubjectService;
import services.model.UserService;

import com.google.inject.Inject;

@Transactional
public class TestDataController extends Controller {

	@Inject
	private static UserService userService;
	
	@Inject
	private static NoveltyService noveltyService;
	
	@Inject
	private static SubjectService subjectService;
	
	@Inject
	private static GradeService gradeService;
	
	@Inject
	private static ChapterService chapterService;
	
	public static Result fill() {
		
		fillSubjects();
		fillGradesHighSchool();
		
		fillChaptersGeography();
		fillChaptersHistory();
		fillChaptersCroatianLiterature();
		
		// Admin fill
		Admin admin = new Admin("lruklic", PasswordHash.createHash("1234"), "Luka", "Ruklić", "ruklic.luka@gmail.com", new Date(System.currentTimeMillis()));
		
		List<Subject> permissions = new ArrayList<>();
		permissions.add(subjectService.findByName("Povijest"));
		permissions.add(subjectService.findByName("Geografija"));
		admin.subjectPermissions = permissions;
		
		admin.clearanceLevel = 3;
		
		userService.save(admin);
		
		
		admin = new Admin("kkolak", PasswordHash.createHash("1234marija"), "Kruno", "Kolak", "kolak.kruno@gmail.com", new Date(System.currentTimeMillis()));
		
		permissions = new ArrayList<>();
		// permissions.add(Subject.HISTORY);
		admin.subjectPermissions = permissions;
		
		admin.clearanceLevel = 2;
		
		userService.save(admin);
		
		Player player = new Player("player", PasswordHash.createHash("1234"), "Igrač", "Igralec", "player@gmail.com", new Date(System.currentTimeMillis()));
		
		userService.save(player);
		
		return ok();
	}
	
	private static Result fillSubjects() {
		
		String[] subjectList = {"Hrvatski jezik - književnost", "Hrvatski jezik - gramatika", "Matematika", "Engleski jezik",
								"Povijest", "Geografija", "Fizika", "Kemija", "Biologija", "Informatika", "Logika", "Sociologija",
								"Filozofija", "Psihologija", "Politika i gospodarstvo", "Glazbena kultura", "Likovna kultura" };
		
		Subject subject;
		for (String s : subjectList) {
			subject = new Subject(s);
			subjectService.save(subject);
		}
		
		return ok();
		
	}
	

	
	private static Result fillGradesHighSchool() {
		String[] gradesList = {"1. razred", "2. razred", "3. razred", "4. razred"};
		
		Grade grade;
		for (String g : gradesList) {
			grade = new Grade(g);
			gradeService.save(grade);
		}
		return ok();
	}
	
	private static void fillChaptersGeography() {
		
		// Prvi razred
		String[] chapterArrayGrade1 = {"Uvod u geografiju", "Zemlja u Sunčevu sustavu i svemiru", "Orijentacija i određivanje položaja", "Predočavanje zemljine površine",
				"Geološke osobine i reljef Zemlje", "Klima", "Vode na Zemlji", "Tla", "Život na Zemlji", "Prirodno-geografska područja"};
		String[] chapterArrayGrade2 = {"Stanovništvo kao čimbenik razvoja i prostornog ustroja", "Naselja i oblici naseljenosti", "Oblici ljudskih djelatnosti", "Ljudske djelatnosti i okoliš"};
		String[] chapterArrayGrade3 = {"Povezivanje svjetskog gospodarstva i njegove posljedice", "Nejednaki regionalni razvoj svijeta", "Glavne integracije u svijetu", "Europska Unija i njene institucije", 
				"Njemačka", "Ujedinjeno Kraljevstvo", "Francuska", "Italija", "SAD", "Kanada", "Japan i azijski tigrovi", "Australija", "Rusija i ostale zemlje u tranziciji", "Kina", "Latinska Amerika", 
				"Meksiko", "Brazil", "Argentina i Čile", "Azija", "Indija", "Jugozapadna Azija", "Afrika i Južnoafrička Republika"};
		String[] chapterArrayGrade4 = {"Uloga geografskog položaja u gospodarskom razvoju RH", "Veličina, granice i oblik teritorija RH", "Reljefna obilježja RH", "Klimatska, pedološka i vegetacijska obilježja RH", 
				"Jadransko more", "Vode na kopnu u RH", "Stanovništvo RH", "Naselja i oblici naseljenosti RH", "Gospodarska obilježja Hrvatske"};
		
		chapterFactory(chapterArrayGrade1, gradeService.findById((long) 1), subjectService.findByName("Geografija"));
		chapterFactory(chapterArrayGrade2, gradeService.findById((long) 2), subjectService.findByName("Geografija"));
		chapterFactory(chapterArrayGrade3, gradeService.findById((long) 3), subjectService.findByName("Geografija"));
		chapterFactory(chapterArrayGrade4, gradeService.findById((long) 4), subjectService.findByName("Geografija"));
		
	}
	
	private static void fillChaptersHistory() {
		
		String[] chapterArray = {"Uvod u povijest", "Prapovijest", "Narodi starog istoka", "Stari Grci",
				"Rim: doba kraljeva i Republika", "Rim: Carstvo"};
		
		chapterFactory(chapterArray, gradeService.findById((long) 1), subjectService.findByName("Povijest"));
		
	}
	
	private static void fillChaptersCroatianLiterature() {
		
		String[] chapterArrayGrade1 = {"Lirika", "Epika", "Drama", "Klasična književnost", "Srednjovjekovna književnost"};
		String[] chapterArrayGrade2 = {"Predrenesansa", "Renesansa" , "Barok" , "Klasicizam" , "Romantizam"};
		String[] chapterArrayGrade3 = {"Protorealizam", "Realizam" , "Modernizam" , "Moderna"};
		String[] chapterArrayGrade4 = {"Avangarda", "I. razdoblje" , "Ekspresionizam" , "II. razdoblje" , "Druga moderna" , "Suvremena književnost"};

		chapterFactory(chapterArrayGrade1, gradeService.findById((long) 1), subjectService.findByName("Hrvatski jezik - književnost"));
		chapterFactory(chapterArrayGrade2, gradeService.findById((long) 2), subjectService.findByName("Hrvatski jezik - književnost"));
		chapterFactory(chapterArrayGrade3, gradeService.findById((long) 3), subjectService.findByName("Hrvatski jezik - književnost"));
		chapterFactory(chapterArrayGrade4, gradeService.findById((long) 4), subjectService.findByName("Hrvatski jezik - književnost"));
		
	}
	
	
	private static void chapterFactory(String[] chapterArray, Grade grade, Subject subject) {
		
		Chapter chapter;
		
		for (String c : chapterArray) {
			chapter = new Chapter(c, grade, subject);
			chapterService.save(chapter);
		}
		
	}
	
}
