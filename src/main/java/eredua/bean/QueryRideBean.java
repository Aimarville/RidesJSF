package eredua.bean;

import java.io.Serializable;
import java.util.*;

import businessLogic.BLFacade;
import domain.Ride;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("query")
@ViewScoped
public class QueryRideBean implements Serializable {
	
	private String departCity;
	private String arrivalCity;
	private Date date;
	private Ride selectedRide;
	private int seats;
	
	List<String> cities = new ArrayList<>();
	List<String> destinationCities = new ArrayList<>();
	List<Ride> rides = new ArrayList<>();
	
	private BLFacade facade = FacadeBean.getBusinessLogic();
	
	public QueryRideBean() {
		cities = facade.getDepartCities();
		onDepartCityChange();
	}
	
	public void onDepartCityChange() {
		if (departCity != null) {
			destinationCities = facade.getDestinationCities(departCity);
/*			if (arrivalCity == null || !destinationCities.contains(arrivalCity)) {
				if (destinationCities.isEmpty()) {
					arrivalCity = null;
				}else {
					arrivalCity = destinationCities.get(0);
				}
			}*/
		}
	}
	
	public void updateRides() {
		if (departCity != null && arrivalCity != null && date != null) {
			rides = facade.getRides(departCity, arrivalCity, date);
		}
	}
	
	public String getDepartCity() { return departCity; }
	
	public void setDepartCity(String departCity) {
		this.departCity = departCity;
		onDepartCityChange();
		updateRides();
	}
	
	public String getArrivalCity() { return arrivalCity; }
	
	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
		updateRides();
	}
	
	public Date getDate() { return date; }
	
	public void setDate(Date date) {
		if (date != null) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        this.date = cal.getTime();
	    } else {
	        this.date = null;
	    }
		updateRides();
	}
	
	public List<String> getCities() { return cities; }
	
	public List<String> getDestinationCities() { return destinationCities; }
	
	public List<Ride> getRides() { return rides; }
	
	public String getDateString() {
		if (date == null) return "";
		return date.toString();
	}
	
	public Ride getSelectedRide() {
		return selectedRide;
	}
	
	public void setSelectedRide(Ride selectedRide) {
		this.selectedRide = selectedRide;
		System.out.println(selectedRide + " aukeratu da");
	}
	
	public int getSeats() {
		return seats;
	}
	
	public void setSeats(int seats) {
		this.seats = seats;
	}
	
	public void queryRide() {
		if (selectedRide != null) {
			if (seats <= selectedRide.getnPlaces()) {
				facade.queryRide(selectedRide, seats);
			
				updateRides();
				seats = 0;
			}else {
				FacesMessage msg = new FacesMessage("Eserleku kopurua pasatu duzu");
				
				FacesContext.getCurrentInstance().addMessage("queryForm:seats", msg);
			}
		}else {
			FacesMessage msg = new FacesMessage("Bidaia bat aukeratu beharko duzu");
			
			FacesContext.getCurrentInstance().addMessage("queryForm:ridesTable", msg);
		}
	}
}
