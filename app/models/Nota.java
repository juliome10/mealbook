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

import play.data.validation.Validation;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Nota extends Model {

    @Id
    public Long id;
    
    @Required
    public String pensamiento;
    
    @Required
    public String emocion;
    
    @Required
    public Long fecha_nota;
    
    @ManyToOne @Required
    public Paciente paciente;
    
    public static Finder<Long, Nota> finder = new Finder<Long,Nota>(Long.class, Nota.class);

    public Nota(JsonNode input){
        super();
        if (input.get("pensamiento") != null){
            this.pensamiento = input.get("pensamiento").asText();
        }
        if (input.get("emocion") != null){
            this.emocion = input.get("emocion").asText();
        }                
        this.fecha_nota = Calendar.getInstance().getTimeInMillis();
    }

	public Nota(Document input) {
        super();
		if (input.getElementsByTagName("pensamiento").item(0).getTextContent() != null){
			this.pensamiento = input.getElementsByTagName("pensamiento").item(0).getTextContent();
		}
		if (input.getElementsByTagName("emocion").item(0).getTextContent() != null){
			this.pensamiento = input.getElementsByTagName("emocion").item(0).getTextContent();
		}
        this.fecha_nota = Calendar.getInstance().getTimeInMillis();
	}
	
	public String devuelveFecha(){
	        Calendar cal = Calendar.getInstance();
	        cal.setTimeInMillis(fecha_nota);
	        int dia = cal.get(Calendar.DAY_OF_MONTH);
	        int mes = cal.get(Calendar.MONTH);
	        int anio = cal.get(Calendar.YEAR);        
	        String hora = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
	        return dia + "" + mes + "" + anio + " " + hora;
	}
	
	public List<String> directValidate() {
        Set<ConstraintViolation<Nota>> violations = Validation.getValidator().validate(this);
        List<String> errors = new ArrayList<String>();
        for(ConstraintViolation<Nota> cv : violations) {
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
	
	public boolean changeData(Nota newData) {
		boolean changed = false;
		
		if (newData.pensamiento != null) {
			this.pensamiento = newData.pensamiento;
			changed = true;
		}
		if (newData.emocion != null) {
			this.emocion = newData.emocion;
			changed = true;
		}
		return changed;
	}
}