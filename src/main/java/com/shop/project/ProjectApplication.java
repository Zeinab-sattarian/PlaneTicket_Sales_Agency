package com.shop.project;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.javafaker.Faker;
import com.opencsv.CSVReader;
import com.shop.project.domain.Airline;
import com.shop.project.domain.City;
import com.shop.project.domain.Flight;
import com.shop.project.domain.Passenger;
import com.shop.project.service.AirlineService;
import com.shop.project.service.CityService;
import com.shop.project.service.FlightService;
import com.shop.project.service.MediaService;
import com.shop.project.service.PassengerService;
import com.shop.project.service.TicketService;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(ProjectApplication.class, args);
		//fill_database(run); //make sure database is empty before using this method
	}

	private static void fill_database(ConfigurableApplicationContext run){
		PassengerService passengerService = run.getBeanFactory().getBean(PassengerService.class);
		AirlineService airlineService = run.getBeanFactory().getBean(AirlineService.class);
		CityService cityService = run.getBeanFactory().getBean(CityService.class);
		FlightService flightService = run.getBeanFactory().getBean(FlightService.class);
		TicketService ticketService = run.getBeanFactory().getBean(TicketService.class);
		String airlineDataFile = "src\\main\\resources\\static\\airlines\\airlines data.csv";
		String cityDataFile = "src\\main\\resources\\static\\cities\\cities data.csv";
		create_fake_passenger(passengerService,100);
		import_airline_data(airlineService,airlineDataFile);
		import_cities(cityService,cityDataFile);
		createRandomFlights(flightService, airlineService, cityService, 200, 339, 21);
		sell50TicketEach(ticketService, passengerService, flightService);
	}


	private static void create_fake_passenger(PassengerService service , int count){
		Faker faker = new Faker();
		for(int i=0;i<count;i++){
			service.createPassenger(faker.name().username(), faker.internet().password(), faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), faker.idNumber().ssnValid(), "default_profile.jpg");
		}
	}

	private static void import_airline_data(AirlineService service , String csvFileAddress){
		try{ 
			FileReader filereader = new FileReader(csvFileAddress); 
	
			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord; 
			csvReader.readNext();

			while ((nextRecord = csvReader.readNext()) != null) { 
				Airline newAirline = new Airline();
				newAirline.setName(nextRecord[0]);
				newAirline.setCallSign(nextRecord[1]);
				newAirline.setHub(nextRecord[2]);
				newAirline.setLogoFileName(nextRecord[3]);
				newAirline.setPassword(newAirline.getName().concat(" pass"));
				service.save(newAirline);
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}

	private static void import_cities(CityService service , String csvFileAddress){
		try { 
			
			FileReader filereader = new FileReader(csvFileAddress); 

			CSVReader csvReader = new CSVReader(filereader); 
			String[] nextRecord; 

			while ((nextRecord = csvReader.readNext()) != null) { 
				City city = new City();
				city.setName(nextRecord[0]);
				service.save(city);
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	
	}

	public static void createRandomFlights(FlightService flightService, AirlineService airlineService, CityService cityService, int numberOfFlights, int numberOfCities, int numberOfAirlines){
		for(int i=0;i<numberOfFlights;i++){
			Airline airline = airlineService.findById(ThreadLocalRandom.current().nextLong(1, numberOfAirlines+1));
			long originId = ThreadLocalRandom.current().nextLong(1, numberOfCities+1);
			City origin = cityService.findById(originId);
			long destinationId;
			do{
				destinationId = ThreadLocalRandom.current().nextLong(1, numberOfCities+1);
			}while(destinationId == originId);
			City destination = cityService.findById(destinationId);
			int numberOfSeats = ThreadLocalRandom.current().nextInt(50, 500);
			int price = ThreadLocalRandom.current().nextInt(70, 1000);
			LocalDateTime departureTime = LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(0, 365)).plusHours(ThreadLocalRandom.current().nextInt(0, 23)).plusMinutes(ThreadLocalRandom.current().nextInt(0, 30)).truncatedTo(ChronoUnit.MINUTES);
			flightService.createFlight(airline, origin, destination, price, numberOfSeats, departureTime);
		}
	}

	public static void sell50TicketEach(TicketService ticketService, PassengerService passengerService, FlightService flightService){

		List<Passenger> passengers = passengerService.findAll();
		int numOfPassengers = passengers.size()-1;
		List<Flight> flights = flightService.findAll();

		for(Flight flight : flights){
			for(int i = 0;i<5;i++){
				int passengerId = ThreadLocalRandom.current().nextInt(0,numOfPassengers);
				ticketService.buyTicket(passengers.get(passengerId), flight);
			}
		}
		


	}

}
