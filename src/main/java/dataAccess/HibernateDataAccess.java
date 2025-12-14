package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import domain.User;
import eredua.JPAUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.validation.ConstraintViolationException;

public class HibernateDataAccess {
	public HibernateDataAccess() {
	}
	
	private boolean isDatabaseInitialized() {
        EntityManager db = JPAUtil.getEntityManager();
        try {
            Long count = (Long) db.createQuery("SELECT COUNT(d) FROM Driver d").getSingleResult();
            return count > 0;
        } finally {
            db.close();
        }
    }
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){
		if (!isDatabaseInitialized()) {
			EntityManager db = JPAUtil.getEntityManager();
	
			try {
				db.getTransaction().begin();
	
			   Calendar today = Calendar.getInstance();
			   
			   int month=today.get(Calendar.MONTH);
			   int year=today.get(Calendar.YEAR);
			   if (month==12) { month=1; year+=1;}  
		    
			   
			    //Create drivers 
				Driver driver1=new Driver("driver1@gmail.com","Aitor Fernandez");
				Driver driver2=new Driver("driver2@gmail.com","Ane Gaztañaga");
				Driver driver3=new Driver("driver3@gmail.com","Test driver");
	
				
				//Create rides
				driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 4, 7);
				driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year,month,6), 4, 8);
				driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 4);
	
				driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year,month,7), 4, 8);
				
				driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 3);
				driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 5);
				driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 5);
	
				driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 3);
	
				
							
				db.persist(driver1);
				db.persist(driver2);
				db.persist(driver3);
	
		
				db.getTransaction().commit();
				System.out.println("Db initialized");
			}
			catch (Exception e){
				if (db.getTransaction().isActive()) {
					db.getTransaction().rollback();
				}
				e.printStackTrace();
			}finally {
				db.close();
			}
		}
	}
	
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	public List<String> getDepartCities(){
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.origin FROM Ride r ORDER BY r.origin", String.class);
			List<String> cities = query.getResultList();
			return cities;
		}finally {
			db.close();
		}
	}
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from){
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.destination FROM Ride r WHERE r.origin=?1 ORDER BY r.destination",String.class);
			query.setParameter(1, from);
			List<String> arrivingCities = query.getResultList();
			return arrivingCities;
		}finally {
			db.close();
		}
		
		
	}
	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException {
		EntityManager db = JPAUtil.getEntityManager();
		
		System.out.println(">> DataAccess: createRide=> from= "+from+" to= "+to+" driver="+driverEmail+" date "+date);
		try {
			if(new Date().compareTo(date)>0) {
				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();
			
			Driver driver = db.find(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			System.out.println("crea addRide");
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			//next instruction can be obviated
			db.persist(driver); 
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			return null;
		}finally {
			db.close();
		}
		
		
	}
	
	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			System.out.println(">> DataAccess: getRides=> from= "+from+" to= "+to+" date "+date);
	
			List<Ride> res = new ArrayList<Ride>();	
			TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.origin=?1 AND r.destination=?2 AND r.date=?3",Ride.class);   
			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, date);
			List<Ride> rides = query.getResultList();
		 	 for (Ride ride:rides){
			   res.add(ride);
			  }
		 	 return res;
		}finally {
		 	 db.close();
		}
	}
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			System.out.println(">> DataAccess: getEventsMonth");
			List<Date> res = new ArrayList<Date>();	
			
			Date firstDayMonthDate= UtilDate.firstDayMonth(date);
			Date lastDayMonthDate= UtilDate.lastDayMonth(date);
					
			
			TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.origin=?1 AND r.destination=?2 AND r.date BETWEEN ?3 and ?4",Date.class);   
			
			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, firstDayMonthDate);
			query.setParameter(4, lastDayMonthDate);
			List<Date> dates = query.getResultList();
		 	 for (Date d:dates){
			   res.add(d);
			  }
		 	 return res;
		}finally {
			db.close();
		}
	}
	
	public User getUser(String mail) {
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			db.getTransaction().begin();
			
			User user = db.find(User.class, mail);
			
			return user;
		}catch (Error e) {
			e.printStackTrace();
			return null;
		}finally {
			db.close();
		}
	}
	
	public boolean createUser(User u) {
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			db.getTransaction().begin();
			
			db.persist(u);
			
			db.getTransaction().commit();
			
			return true;
		}catch (Error e) {
			e.printStackTrace();
			return false;
		}finally {
			db.close();
		}
	}
	
	public void queryRide(Ride ride, int seats, User user) {
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			db.getTransaction().begin();
			
			Ride thisRide = db.find(Ride.class, ride.getRideNumber());
			User thisUser = db.find(User.class, user.getMail());
			
			thisRide.setBetMinimum(thisRide.getnPlaces() - seats);
			
			thisRide.getUserList().add(thisUser);
			
			db.merge(thisRide);
			
			thisUser.getRideList().add(thisRide);
			db.merge(thisUser);
			
			db.getTransaction().commit();
		}finally {
			db.close();
		}
	}
	
	public List<Ride> getUserRides(User u) {
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
			User user = db.find(User.class, u.getMail());
			
			if (user != null && user.getRideList().size() > 0) {
				return user.getRideList();
			}
			return null;
		}finally {
			db.close();
		}
	}
	
	public List<Ride> getArrivalCities() {
		EntityManager db = JPAUtil.getEntityManager();
		
		try {
	
			List<Ride> res = new ArrayList<Ride>();	
			TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r",Ride.class);
			List<Ride> rides = query.getResultList();
		 	 for (Ride ride:rides){
			   res.add(ride);
			  }
		 	 return res;
		}finally {
		 	 db.close();
		}
	}
}