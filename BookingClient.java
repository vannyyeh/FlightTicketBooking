/* MULTITHREADING <Flight.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * <Vanny Yeh>
 * <jy24633>
 * <17810>
 * Slip days used: <1>
 * Fall 2021
 */
package assignment6;

import assignment6.Flight.SeatClass;

import java.util.*;

public class BookingClient {

	public Map<String, SeatClass[]> offices;
	public Flight flight;
	public int currentCustomer = 1; //check which customer is dealing with 

	/**
	 * @param offices maps ticket office id to seat class preferences of customers
	 *                in line
	 * @param flight  the flight for which tickets are sold for
	 */

	public BookingClient(Map<String, SeatClass[]> offices, Flight flight) {
		this.offices = offices;
		this.flight = flight;
	}

	/**
	 * Starts the ticket office simulation by creating (and starting) threads for
	 * each ticket office to sell tickets for the given flight
	 *
	 * @return list of threads used in the simulation, should have as many threads
	 *         as there are ticket offices
	 */
	public List<Thread> simulate() {
		List<Thread> threadArray = new ArrayList<>(); //To keep tracking all the thread
		for (Map.Entry<String, SeatClass[]> entry : offices.entrySet()) { //Go through all single offices and check the inquries
			TicketOffice newTicket = new TicketOffice(this, entry.getKey(), entry.getValue());
			threadArray.add(newTicket);
			newTicket.run(); 
		}

		for (Thread thread : threadArray) {
			try {
				thread.join(); //Join it and wait to be finished
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return threadArray; //When all the thread finished, it exits
	}

	public Integer popCustomer() {
		return currentCustomer++;
	}

	public static void main(String[] args) {
		HashMap<String, SeatClass[]> offices = new HashMap<String, SeatClass[]>();
		offices.put("TO1", new SeatClass[] { SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.BUSINESS,
				SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.ECONOMY });
		offices.put("TO2", new SeatClass[] { SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.BUSINESS,
				SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.ECONOMY });
		offices.put("TO3", new SeatClass[] { SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.BUSINESS,
				SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.ECONOMY });
		offices.put("TO4", new SeatClass[] { SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.BUSINESS,
				SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.ECONOMY });
		offices.put("TO5", new SeatClass[] { SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.BUSINESS,
				SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.ECONOMY });
		offices.put("TO6", new SeatClass[] { SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.BUSINESS,
				SeatClass.ECONOMY, SeatClass.BUSINESS, SeatClass.ECONOMY });

		Flight flight = new Flight("TR123", 1, 1, 1);

		BookingClient bc = new BookingClient(offices, flight);

		System.out.println("Starting threads!");
		List<Thread> threadList = bc.simulate();
		System.out.println("All threads finished. Loading logs...");
		System.out.println("For reference:\nFirst class seats start at row: " + flight.fistClassStartRow
				+ "\nBusiness class seats start at row: " + flight.businessClassStartRow
				+ "\nEconomy class seats start at row: " + flight.economyClassStartRow);

		List<Flight.Ticket> tickets = flight.getTransactionLog();
		for (Flight.Ticket ticket : tickets) {
			System.out.println(ticket);
		}
		if (flight.seatsQueue.size() == 0) {
			System.out.println("Sorry, we are sold out!");
		}
	}

	//
	public class TicketOffice extends Thread {
		private final BookingClient client;
		private final String officeId;
		private final SeatClass[] seatTypes;

		public TicketOffice(BookingClient client, String officeId, SeatClass[] seatTypes) {
			this.client = client;
			this.officeId = officeId;
			this.seatTypes = seatTypes;
		}

		public void run() {
			for (SeatClass seatClass : seatTypes) { //Iterate the highest class level that customer wants
				//When this tread is in the object, no other and enter, have to wait until it's finished
				synchronized (client) {
					Flight.Seat seat = client.flight.getNextAvailableSeat(seatClass);
					Flight.Ticket ticket = client.flight.printTicket(officeId, seat, client.popCustomer());
					if (ticket == null) {
						continue;
					}
				}
				try {
					Thread.sleep(client.flight.getPrintDelay()); //It sleep when there's some one in it, wake up when it's term
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
