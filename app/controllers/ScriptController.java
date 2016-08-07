package controllers;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.Document;

import converter.HtmlPdfConverter;
import forms.ScriptForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.script;

public class ScriptController extends Controller {

	public static Result submit() {
		
		Form<ScriptForm> scriptForm = Form.form(ScriptForm.class).bindFromRequest();
		
		String scriptContent = scriptForm.get().script;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Document doc = new Document();
		
		// this method should not be called from outside of htmlpdfconverter, set as private and call converthtmltopdf(list)
		HtmlPdfConverter.convertHtmlToPdf(baos, doc, scriptContent);
		
		byte[] output = baos.toByteArray();
		
		response().setContentType("application/x-download");
		response().setHeader("Content-disposition", "attachment; filename=templates.pdf");
		return ok(output);
	}
	
	public static Result add() {
		return ok(script.render());
	}
	
}
