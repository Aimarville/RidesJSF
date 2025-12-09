package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.User;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation  implements BLFacade {
	HibernateDataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new HibernateDataAccess();
		    
		    dbManager.initializeDB();
		//

		
	}
	
    public BLFacadeImplementation(HibernateDataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		
		dbManager=da;
		
		dbManager.initializeDB();
	}
    
    
    /**
     * {@inheritDoc}
     */
    public List<String> getDepartCities(){
    		
		
		 List<String> departLocations=dbManager.getDepartCities();		

		
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	public List<String> getDestinationCities(String from){
			
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
		
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	public List<Ride> getRides(String from, String to, Date date){
		
		List<Ride>  rides=dbManager.getRides(from, to, date);
		
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		
		return dates;
	}
	
	public boolean registerUser(String name, String surname, String mail, String password) {
		
		User user = dbManager.getUser(mail);
		
		boolean done = false;
		if (user == null) {
			done = dbManager.createUser(name, surname, mail, password);
		}
		
		return done;
	}
	
	public boolean loginUser(String mail, String password) {
		boolean buelta;
		
		User user = dbManager.getUser(mail);
		
		if (user != null) {
			if (user.getPassword().equals(password))
				buelta = true;
			else
				buelta = false;
		}else
			buelta = false;
		
		return buelta;
	}
	
	public void queryRide(Ride ride, int seats) {
		dbManager.queryRide(ride, seats);
	}

	/**
	 * {@inheritDoc}
	 */
	 public void initializeBD(){
    	
		dbManager.initializeDB();
		
	}

}

