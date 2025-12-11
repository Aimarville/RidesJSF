package domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
public class User implements Serializable {
	@Id
	private String mail;
	private String name;
	private String surname;
	private String password;
	
	@ManyToMany
	@JoinTable(
		    name = "user_ride",
		    joinColumns = @JoinColumn(name = "user_mail"),
		    inverseJoinColumns = @JoinColumn(name = "ride_number")
		)
	private List<Ride> rideList;
	
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
	
	public List<Ride> getRideList() {
		return rideList;
	}
	
	public void setRide(List<Ride> rideList) {
		this.rideList = rideList;
	}

	@Override
	public String toString() {
		return name + ";" + surname + ";" + mail;
	}
}
