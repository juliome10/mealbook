package controllers;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import models.Nota;
import models.Paciente;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

public class Notas extends Controller {

	   //Contenido JSON?
       private static boolean contentIsJson() {
           String content = request().getHeader("Content-Type");
           return content.startsWith("application/json");
       }
       
       //Contenido XML?
       private static boolean contentIsXml() {
           String content = request().getHeader("Content-Type");
           return (content.startsWith("application/xml") || content.startsWith("text/xml"));
       }
       
       public static Result create(Long idPaciente) {
           Result res = ok();
           //Creación de la nota con los datos recibidos
           Nota nota = getNotaFromBody();
           if (nota == null) {
               res = badRequest(errorJson(1, "unsupported_format"));
           }else {
    	   	   //Búsqueda del paciente por su id
    	   	   Paciente paciente = Paciente.finder.byId(idPaciente);
    	   	   //Asignación de la nota al paciente
    	   	   nota.paciente = paciente;
               List<String> errors = nota.validateAndSave();
               if (errors.size() == 0) {
        	   	   res = ok();
               }else {
                   res = badRequest();
               }
           }
           
           return res;
       }
       
       private static Nota getNotaFromBody() {
           Nota nota;
           
           if (contentIsJson()) {
                   JsonNode input = request().body().asJson();
                   nota = new Nota(input);
           }
           else if (contentIsXml()) {
                   Document input = request().body().asXml();
                   nota = new Nota(input);
           }
           else {
                   nota = null;
           }
           
           return nota;
       }
       
       private static JsonNode errorJson(Integer code, String message) {
           ObjectNode node = Json.newObject();
           node.put("code", code);
           node.put("message", Messages.get(message));
           return node;
       }
       
       public static Result update(Long id) {
   		Result res = null;
   		
   		Nota nota = Nota.finder.byId(id);
   		if (nota == null) {
   			res = notFound();
   		}else{
   			Nota newNota = getNotaFromBody();
   			if (newNota == null) {
   				res = badRequest(errorJson(1, "unsupported_format"));
   			}
   			else {
   				if (nota.changeData(newNota)) {
   					nota.save();
   					res = ok();
   				}
   				else {
   					res = status(NOT_MODIFIED);
   				}
   			}
   		}
   		
   		return res;
   	}
   	
   	public static Result delete(Long id) {
   		Result res = null;
   		
   		Nota nota = Nota.finder.byId(id);
   		if (nota == null) {
   			res = notFound();
   		}else{
   			nota.delete();
   			res = ok();
   		}

   		return res;
   	}
   	
   	public static Result retrieve(Long id){
		Result res;
		
		Nota nota = Nota.finder.byId(id);
		
		if (nota == null) {
			res = notFound();
		}else{
			if (acceptsJson()){
				res = okWithJsonContent(views.txt._nota.render(nota));
			}else if (acceptsXml()){
				res = ok(views.xml._nota.render(nota));
			}else{
				res = badRequest(errorJson(1, "unsupported_format"));
			}
		}
		
		return res;	
	}
   	
   	//Acepta JSON?
  	private static boolean acceptsJson() {
  		return request().accepts("application/json");
  	}
  	
  	//Acepta XML?
  	private static boolean acceptsXml() {
  		return (request().accepts("application/xml") || request().accepts("text/xml"));
  	}
  	
  	//Enviar contenido en formato JSON
  	private static Result okWithJsonContent(Content c) {
  		response().setContentType("application/json");
  		return ok(c);
  	}
}
 
