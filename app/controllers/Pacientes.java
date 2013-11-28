package controllers;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import models.Nota;
import models.Paciente;
import models.Terapeuta;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

public class Pacientes extends Controller{

	//Acepta JSON?
	private static boolean acceptsJson() {
		return request().accepts("application/json");
	}
	
	//Acepta XML?
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
	
	//Enviar contenido en formato JSON
	private static Result okWithJsonContent(Content c) {
		response().setContentType("application/json");
		return ok(c);
	}
	
	public static Result retrieve(Long id){
		Result res;
		
		Paciente paciente = Paciente.finder.byId(id);
		
		if (paciente == null) {
			res = notFound();
		}else{
			if (acceptsJson()){
				res = okWithJsonContent(views.txt._paciente.render(paciente));
			}else if (acceptsXml()){
				res = ok(views.xml._paciente.render(paciente));
			}else{
				res = badRequest(errorJson(1, "unsupported_format"));
			}
		}
		
		return res;	
	}
	
	private static Paciente getPacienteFromBody() {
		Paciente paciente;
		
		if (contentIsJson()) {
			JsonNode input = request().body().asJson();
			paciente = new Paciente(input);
		}else if (contentIsXml()) {
			Document input = request().body().asXml();
			paciente = new Paciente(input);
		}else {
			paciente = null;
		}
		
		return paciente;
	}
	
	public static Result create(String dni) {
		Result res = ok();
		//Creación del paciente con los datos recibidos del body
		Paciente paciente = getPacienteFromBody();
		
		if (paciente == null) {
			res = badRequest(errorJson(1, "unsupported_format"));
		}else {
			//Búsqueda del terapeuta por su dni
			Terapeuta terapeuta = Terapeuta.finder.where().eq("dni", dni).findUnique();
			//Asignación
			paciente.terapeuta = terapeuta;
			
			List<String> errors = paciente.validateAndSave();
			if (errors.size() == 0) {
				response().setHeader(LOCATION, routes.Pacientes.retrieve(paciente.id).absoluteURL(request()));
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
	
	public static Result update(Long id) {
		Result res = null;
		
		Paciente paciente = Paciente.finder.byId(id);
		if (paciente == null) {
			res = notFound();
		}else{
			Paciente newPaciente = getPacienteFromBody();
			if (newPaciente == null) {
				res = badRequest(errorJson(1, "unsupported_format"));
			}else{
				if (paciente.changeData(newPaciente)) {
					paciente.save();
					res = ok();
				}else{
					res = status(NOT_MODIFIED);
				}
			}
		}
		
		return res;
	}
	
	public static Result delete(Long id) {
		Result res = null;
		
		Paciente paciente = Paciente.finder.byId(id);
		if (paciente == null) {
			res = notFound();
		}else{
			paciente.delete();
			res = ok();
		}

		return res;
	}
	
	public static Result index() {
		Result res;
		List<Paciente> lista = Paciente.findAll();
		if (acceptsJson()){
			res = okWithJsonContent(views.txt.pacientes.render(lista));
		}else if (acceptsXml()){
			res = ok(views.xml.pacientes.render(lista));
		}else{
			res = badRequest(errorJson(1, "unsupported_format"));
		}
		return res;
    }
	
	public static Result notas(Long id){
		Result res;
		Paciente paciente = Paciente.finder.where().eq("id",id).findUnique();
		if (paciente == null) {
			res = notFound();
		}else{
	        List<Nota> lista = paciente.notas;
	        if (acceptsJson()){
	        	res = okWithJsonContent(views.txt.notas.render(lista));
	        }else if (acceptsXml()){
	        	res = ok(views.xml.notas.render(lista)); 
	        }else{
	        	res = badRequest(errorJson(1, "unsupported_format"));
	        }
		}
		
        return res;
    }
	
}
