package controllers;

import java.util.List;

import models.Terapeuta;
import play.mvc.*;
import views.html.index;

public class Terapeutas extends Controller{

	//Enviar JSON?
	private static boolean acceptsJson() {
		return request().accepts("application/json");
	}
	
	//Enviar XML?
	private static boolean acceptsXml() {
		return (request().accepts("application/xml") || request().accepts("text/xml"));
	}

	//Recibido JSON?
	private static boolean contentIsJson() {
		String content = request().getHeader("Content-Type"); 
		return content.startsWith("application/json");
	}
	
	//Recibido XML?
	private static boolean contentIsXml() {
		String content = request().getHeader("Content-Type"); 
		return (content.startsWith("application/xml") || content.startsWith("text/xml"));
	}
	
	public static Result create() {
        return ok(index.render("Your new application is ready."));
    }
	
	public static Result retrieve(String dni) {
        return ok(index.render("Your new application is ready."));
    }
	
	public static Result update(String dni) {
        return ok(index.render("Your new application is ready."));
    }
	
	public static Result delete(String id) {
        return ok(index.render("Your new application is ready."));
    }
	
	public static Result index() {
		List<Terapeuta> lista = Terapeuta.findAll();
		return ok(views.xml.terapeutas.render(lista));
    }
}
