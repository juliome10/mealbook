package controllers;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import models.Paciente;
import models.Terapeuta;
import play.i18n.Messages;
import play.libs.Json;
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
	
	private static Terapeuta getTerapeutaFromBody() {
		Terapeuta terapeuta;
		
		if (contentIsJson()) {
			JsonNode input = request().body().asJson();
			terapeuta = new Terapeuta(input);
		}
		else if (contentIsXml()) {
			Document input = request().body().asXml();
			terapeuta = new Terapeuta(input);
		}
		else {
			terapeuta = null;
		}
		
		return terapeuta;
	}
	
	public static Result create() {
		Result res = ok();
		Terapeuta terapeuta = getTerapeutaFromBody();
		if (terapeuta == null) {
			res = badRequest(errorJson(1, "unsupported_format"));
		}else {
			List<String> errors = terapeuta.validateAndSave();
			if (errors.size() == 0) {
				response().setHeader(LOCATION, routes.Terapeutas.retrieve(terapeuta.dni).absoluteURL(request()));
				res = ok();
			}else {
				res = badRequest();
			}
		}
		
		return res;
    }
	
	public static Result retrieve(String dni) {
		Result res;
		
		Terapeuta terapeuta = Terapeuta.finder.where().eq("dni", dni).findUnique();
		
		if (terapeuta == null) {
			res = notFound();
		}else {
			res = ok(views.xml._terapeuta.render(terapeuta));
		}
		
		return res;
	}
	
	public static Result update(String dni) {
		Result res = null;
		
		Terapeuta terapeuta = Terapeuta.finder.where().eq("dni", dni).findUnique();
		if (terapeuta == null) {
			res = notFound();
		}
		else {
			Terapeuta newTerapeuta = getTerapeutaFromBody();
			if (newTerapeuta == null) {
				res = badRequest(errorJson(1, "unsupported_format"));
			}
			else {
				if (terapeuta.changeData(newTerapeuta)) {
					terapeuta.save();
					res = ok();
				}
				else {
					res = status(NOT_MODIFIED);
				}
			}
		}
		
		return res;
	}
	
	public static Result delete(String dni) {
		Result res = null;
		
		Terapeuta terapeuta = Terapeuta.finder.where().eq("dni", dni).findUnique();
		if (terapeuta == null) {
			res = notFound();
		}
		else {
			terapeuta.delete();
			res = ok();
		}

		return res;
	}
	
	public static Result index() {
		List<Terapeuta> lista = Terapeuta.findAll();
		return ok(views.xml.terapeutas.render(lista));
    }
	
	private static JsonNode errorJson(Integer code, String message) {
		ObjectNode node = Json.newObject();
		node.put("code", code);
		node.put("message", Messages.get(message));
		return node;
	}
	
	public static Result pacientes(String dni) {
		List<Paciente>lista = null;
		return ok(views.xml.pacientes.render(lista));
	}
}
