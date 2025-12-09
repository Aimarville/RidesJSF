package domain;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class User implements Serializable {
	@Id
	private String mail;
	private String name;
	private String surname;
	private String password;
	
	public User() {
		super();
	}
	
	public User(String name, String surname, String mail, String password) {
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		this.password = password;
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

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return name + ";" + surname + ";" + mail;
	}
}
