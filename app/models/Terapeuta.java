package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Terapeuta extends Model{

	@Id
	public Long id;
	
	@Required
	public String dni;
	
	@Required
	public String email;
	
	public static Finder<Long, Terapeuta> finder = new Finder<Long, Terapeuta>(Long.class, Terapeuta.class);

	public Terapeuta (String dni, String email){
		this.dni = dni;
		this.email = email;
	}
	
	public static List<Terapeuta> findAll() {
		return finder.findList();
	}
	
}
