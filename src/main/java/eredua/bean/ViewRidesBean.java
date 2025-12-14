package eredua.bean;

import java.io.Serializable;
import java.util.*;

import businessLogic.BLFacade;
import domain.Ride;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("viewRides")
@ViewScoped
public class ViewRidesBean implements Serializable {
	
	private List<Ride> rides = new ArrayList<>();
	
	private List<Ride> cities = new ArrayList<>();
	private List<String> citiesNames = new ArrayList<>();
	private static String arrivalCity;
	private int kop;
	
	private String globalArrivalCity;
	
	private BLFacade facade = FacadeBean.getBusinessLogic();

	public List<Ride> getRides() {
		return facade.getUserRides();
	}
	
	public List<Ride> getCities() {
		getRidesAll();
		changeCities();
		return cities;
	}
	
	public void setCities(List<Ride> cities) {
		this.cities = cities;
	}
	
	public String getArrivalCity() {
		return arrivalCity;
	}
	
	public void setArrivalCity(String arrivalCity) {
		globalArrivalCity = arrivalCity;
		this.arrivalCity = arrivalCity;
	}
	
	public List<String> getCitiesNames() {
		getRidesAll();
		List<String> tempCity = new ArrayList<>();
		for (Ride r : cities) {
			if (!tempCity.contains(r.getTo()))
				tempCity.add(r.getTo());
		}
		citiesNames = tempCity;
		return citiesNames;
	}
	
	public void setCitiesNames(List<String> citiesNames) {
		this.citiesNames = citiesNames;
	}
	
	public int getKop() {
		kop = cities.size();
		return kop;
	}
	
	public void setKop(int kop) {
		this.kop = kop;
	}
	
	public void getRidesAll() {
		List<Ride> tempCities = facade.getDestinationCities();
		cities = tempCities;
	}
	
	public void printValue() {
		System.out.println(arrivalCity);
	}
	
	public void changeCities() {
		System.out.println(arrivalCity);
		List<Ride> temp = cities;
		cities = new ArrayList<>();
		for (Ride r : temp) {
			if (r.getTo().equals(arrivalCity))
				cities.add(r);
		}
		System.out.println(cities);
	}
}
