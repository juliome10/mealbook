package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.ConstraintViolation;

import org.codehaus.jackson.JsonNode;
import org.w3c.dom.Document;

import com.avaje.ebean.validation.Email;

import play.data.validation.Validation;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Paciente extends Model{

	@Id
	public Long id;
	
	@Required @Email
	public String email;
	
	@Required
	public String password;
	
	@Required
	public Long fecha_reg;
	
	@Required
	public String nombre;
	
	@Required
	public Long fecha_nac;
	
	@Required
	public String sexo;
	
	@Required
	public Double altura;
	
	public String medicamentos;
	
	@ManyToOne
	public Terapeuta terapeuta;
	
	public static Finder<Long, Paciente> finder = new Finder<Long,Paciente>(Long.class, Paciente.class);
	
	public Paciente(JsonNode input) {
		super();
		if (input.get("nombre") != null){
			this.nombre = input.get("nombre").asText();
		}
		if (input.get("password") != null){
			this.password = input.get("password").asText();
		}
		if (input.get("email") != null){
			this.email = input.get("email").asText();
		}
		if (input.get("sexo") != null){
			this.sexo = input.get("sexo").asText();
		}
		if (input.get("altura") != null){
			this.altura = Double.valueOf(input.get("altura").asText());
		}
		if (input.get("medicamentos") != null){
			this.medicamentos = input.get("medicamentos").asText();
		}
		if (input.get("fecha_nac") != null){
			String[] valores = input.get("fecha_nac").asText().split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.valueOf(valores[2]), Integer.valueOf(valores[1]), Integer.valueOf(valores[0]));
			this.fecha_nac = cal.getTimeInMillis();
		}
		this.fecha_reg = Calendar.getInstance().getTimeInMillis();
	}
	
	public Paciente(Document input) {
		super();
		this.nombre = input.getElementsByTagName("nombre").item(0).getTextContent();
	}
	
	public String devuelveFecha(String f){
		String hora = null;
		Calendar cal = Calendar.getInstance();
		if (f.equalsIgnoreCase("reg")){
			cal.setTimeInMillis(fecha_reg);
			hora = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		}else{
			cal.setTimeInMillis(fecha_nac);
			hora = "";
		}
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);	
		return dia + "-" + mes + "-" + anio + " " + hora;
	}
	
	public List<String> directValidate() {
		Set<ConstraintViolation<Paciente>> violations = Validation.getValidator().validate(this);
		List<String> errors = new ArrayList<String>();
		for(ConstraintViolation<Paciente> cv : violations) {
			errors.add(cv.getMessage());
		}
		String violation = this.validate();
		if (violation != null) {
			errors.add(violation);
		}
		return errors;
	}
	
	public String validate() {
		return null;
	}

	public List<String> validateAndSave() {
		List<String> errors = directValidate();
		if (errors.size() == 0) {
			this.save();
		}
		return errors;
	}
	
	
}
