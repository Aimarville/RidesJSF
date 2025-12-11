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
	
	private BLFacade facade = FacadeBean.getBusinessLogic();

	public List<Ride> getRides() {
		return facade.getUserRides();
	}
}
