package eredua.bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("registerBean")
@ViewScoped
public class RegisterBean implements Serializable {
	private String name;
	private String surname;
	private String mail;
	private String username;
	private String password;
	
	private BLFacade facade = FacadeBean.getBusinessLogic();
	
	public RegisterBean() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String register() {
		boolean egina = facade.registerUser(name, surname, mail, password);
		if (egina)
			return "home";
		else {
			FacesMessage msg = new FacesMessage("Korreo honi lotutako erabiltzaile bat jadanik existitzen da");
			
			FacesContext.getCurrentInstance().addMessage("registerForm:mail", msg);
			
			return null;
		}
	}
}
