package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.ConstraintViolation;

import org.codehaus.jackson.JsonNode;
import org.w3c.dom.Document;

import com.avaje.ebean.validation.Email;

import play.data.validation.Validation;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;


@Entity
public class Terapeuta extends Model{

	@Id
	public Long id;
	
	@Column(unique=true)
	public String dni;
	
	@Required @Email
	public String email;
	
	@Required
	public String password;
	
	@Required
	public String nombre;
	
	@Required
	public String apellidos;
	
	@Required
	public Long fecha_reg;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="terapeuta")
	public List<Paciente> pacientes = new ArrayList<Paciente>();
	
	public static Finder<Long, Terapeuta> finder = new Finder<Long, Terapeuta>(Long.class, Terapeuta.class);

	public Terapeuta (String dni, String email){
		this.dni = dni;
		this.email = email;
	}
	
	public Terapeuta(JsonNode input) {
		super();
		if (input.get("nombre") != null){
			this.nombre = input.get("nombre").asText();
		}
		if (input.get("apellidos") != null){
			this.apellidos = input.get("apellidos").asText();

		}
		if (input.get("password") != null){
			this.password = input.get("password").asText();
		}
		if (input.get("email") != null){
			this.email = input.get("email").asText();
		}
		if (input.get("dni") != null){
			this.dni = input.get("dni").asText();
		}
		this.fecha_reg = Calendar.getInstance().getTimeInMillis();
	}
	
	public Terapeuta(Document input) {
		super();
		this.nombre = input.getElementsByTagName("nombre").item(0).getTextContent();
	}
	
	public static List<Terapeuta> findAll() {
		return finder.findList();
	}
	
	public List<String> directValidate() {
		Set<ConstraintViolation<Terapeuta>> violations = Validation.getValidator().validate(this);
		List<String> errors = new ArrayList<String>();
		for(ConstraintViolation<Terapeuta> cv : violations) {
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
	
	public String devuelveFecha(){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(fecha_reg);
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);	
		String hora = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		return dia + "-" + mes + "-" + anio + " " + hora;
	}
	
	public boolean changeData(Terapeuta newData) {
		boolean changed = false;
		
		if (newData.nombre != null) {
			this.nombre = newData.nombre;
			changed = true;
		}
		if (newData.apellidos != null) {
			this.apellidos = newData.apellidos;
			changed = true;
		}
		if (newData.email != null) {
			this.email = newData.email;
			changed = true;
		}
		if (newData.password != null) {
			this.password = newData.password;
			changed = true;
		}
		if (newData.dni != null) {
			this.dni = newData.dni;
			changed = true;
		}
		return changed;
	}
}
