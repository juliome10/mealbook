package controllers;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import models.Paciente;
import models.Terapeuta;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Pacientes extends Controller{

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
	
	public static Result retrieve(String email){
		Result res;
		
		Paciente paciente = Paciente.finder.where().eq("email", email).findUnique();
		
		if (paciente == null) {
			res = notFound();
		}else {
			res = ok(views.xml._paciente.render(paciente));
		}
		
		return res;	}
	
	private static Paciente getPacienteFromBody() {
		Paciente paciente;
		
		if (contentIsJson()) {
			JsonNode input = request().body().asJson();
			paciente = new Paciente(input);
		}
		else if (contentIsXml()) {
			Document input = request().body().asXml();
			paciente = new Paciente(input);
		}
		else {
			paciente = null;
		}
		
		return paciente;
	}
	
	public static Result create() {
		Result res = ok();
		Paciente paciente = getPacienteFromBody();
		if (paciente == null) {
			res = badRequest(errorJson(1, "unsupported_format"));
		}else {
			List<String> errors = paciente.validateAndSave();
			if (errors.size() == 0) {
				response().setHeader(LOCATION, routes.Terapeutas.retrieve(paciente.email).absoluteURL(request()));
				res = ok();
			}else {
				res = badRequest();
			}
		}
		
		return res;
    }
	
	private static JsonNode errorJson(Integer code, String message) {
		ObjectNode node = Json.newObject();
		node.put("code", code);
		node.put("message", Messages.get(message));
		return node;
	}
	
}
