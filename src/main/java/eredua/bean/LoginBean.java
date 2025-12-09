package eredua.bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("loginBean")
@ViewScoped
public class LoginBean implements Serializable {
	private String mail;
	private String password;
	
	private BLFacade facade = FacadeBean.getBusinessLogic();
	
	public LoginBean() {
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String login() {
		boolean u = facade.loginUser(mail, password);
		
		if (u)
			return "home";
		else {
			FacesMessage msg = new FacesMessage("Erabiltzailea edo pasahitza ez dira egokiak");
			
			FacesContext.getCurrentInstance().addMessage("loginForm:password", msg);
			
			return null;
		}
	}
}
