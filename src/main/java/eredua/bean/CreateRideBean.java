package eredua.bean;

import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import businessLogic.BLFacade;
import domain.Driver;

@Named("ride")
@SessionScoped
public class CreateRideBean implements Serializable {
	
	private String departCity;
	private String arrivalCity;
	private int seats;
	private float price;
	private Date date;
	private Date today = new Date();
	
	private BLFacade facade = FacadeBean.getBusinessLogic();
	
	public Date getToday() {
		return today;
	}
	
	public void setToday(Date today) {
		this.today = today;
	}

    public String getDepartCity() {
		return departCity;
	}

	public void setDepartCity(String departCity) {
		this.departCity = departCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CreateRideBean() {}
	
	public String create() {
		try {
			facade.createRide(departCity, arrivalCity, date, seats, price, "driver1@gmail.com");
			reset();
		}catch (Exception e) {
			System.out.println("Exception ride exist");
		}
		return null;
	}
	
	public void reset() {
		departCity = null;
		arrivalCity = null;
		date = null;
		seats = 0;
		price = 0;
	}
}
