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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Flight {
	/**
	 * the delay time you will use when print tickets
	 */
	private int printDelay; // 50 ms. Use it to fix the delay time between prints.
	private SalesLogs log;

	
	// private int currentClass = 1;
	public Queue<Seat> seatsQueue = new LinkedList<>();
	private String flightNo;
	private int firstNumRows;
	private int businessNumRows;
	private int economyNumRows;

	public boolean soldOut = false;

	// Seat Map
//	private ArrayList<String> firstClassSeatList;
//	private ArrayList<String> businessClassSeatList;
//	private ArrayList<String> economyClassSeatList;
	public int fistClassStartRow;
	public int businessClassStartRow;
	public int economyClassStartRow;

	public static void main(String[] args) {
		Flight flight = new Flight("1", 2, 2, 2);
		Seat seat = flight.getNextAvailableSeat(SeatClass.FIRST);
		flight.printTicket("101", seat, 1);
	}

	public Flight(String flightNo, int firstNumRows, int businessNumRows, int economyNumRows) {
		this.printDelay = 50;// 50 ms. Use it to fix the delay time between
		this.log = new SalesLogs();

		this.flightNo = flightNo;
		this.firstNumRows = firstNumRows;
		this.businessNumRows = businessNumRows;
		this.economyNumRows = economyNumRows;

		// making the seat map
		int fistClassStartRow = 1;
		createFirstClassSeatList(fistClassStartRow, firstNumRows);
		this.fistClassStartRow = fistClassStartRow;
		int businessClassStartRow;
		businessClassStartRow = (fistClassStartRow + firstNumRows); //Following by the order
		this.businessClassStartRow = businessClassStartRow;
		createBusinessClassSeatList(businessClassStartRow, businessNumRows);
		int economyClassStartRow;
		economyClassStartRow = (businessClassStartRow + businessNumRows);
		this.economyClassStartRow = economyClassStartRow;
		createEconomyClassSeatList(economyClassStartRow, economyNumRows);

	}

	// Create seat class list for each class
	private void createFirstClassSeatList(int startRow, int firstNumRows) {
//		String item;
		for (int i = startRow; i <= firstNumRows; i++) {
			for (SeatLetter letter : SeatLetter.values()) {
				if (letter.toString().contains("A") || letter.toString().contains("B")
						|| letter.toString().contains("E") || letter.toString().contains("F")) {
					Seat seat = new Seat(SeatClass.FIRST, i, letter);
//					item = seat.letter.toString() + Integer.toString(seat.row);
//					firstClassSeatList.add(item);
					seatsQueue.add(seat);
				}
			}
		}
	}

	//It starts from the startRow and includes the business row, otherwise it will count less
	private void createBusinessClassSeatList(int startRow, int businessNumRows) {
//		String item;
		for (int i = 0; i < businessNumRows; i++) {
			for (SeatLetter letter : SeatLetter.values()) {
				Seat seat = new Seat(SeatClass.BUSINESS, startRow + i, letter);
//					item = seat.letter.toString() + Integer.toString(seat.row);
//					businessClassSeatList.add(item);
				seatsQueue.add(seat);
			}
		}
	}

	private void createEconomyClassSeatList(int startRow, int economyNumRows) {
//		String item;
		for (int i = 0; i < economyNumRows; i++) {
			for (SeatLetter letter : SeatLetter.values()) {
				Seat seat = new Seat(SeatClass.ECONOMY, startRow + i, letter);
//					item = seat.letter.toString() + Integer.toString(seat.row);
//					economyClassSeatList.add(item);
				seatsQueue.add(seat);
			}
		}
	}

	public void setPrintDelay(int printDelay) {
		this.printDelay = printDelay;
	}

	public int getPrintDelay() {
		return printDelay;
	}

	/**
	 * Returns the next available seat not yet reserved for a given class
	 *
	 * @param seatClass a seat class(FIRST, BUSINESS, ECONOMY)
	 * @return the next available seat or null if flight is full
	 */
	//Using switch instead of using if statement for enum
	public Seat getNextAvailableSeat(SeatClass seatClass) {
		int startRow = 1;

		switch (seatClass) {
		case FIRST:
			startRow = fistClassStartRow;
			break;
		case BUSINESS:
			startRow = businessClassStartRow;
			break;
		case ECONOMY:
			startRow = economyClassStartRow;
			break;
		}

		for (Seat seat : seatsQueue) {
			if (seat.row >= startRow) {
				seatsQueue.remove(seat);
				this.log.addSeat(seat);
				return seat;
			}
		}
		return null;
	}

	/**
	 * Prints a ticket to the console for the customer after they reserve a seat.
	 *
	 * @param seat a particular seat in the airplane
	 * @return a flight ticket or null if a ticket office failed to reserve the seat
	 */
	//Add into the log when there is an available seat
	public Ticket printTicket(String officeId, Seat seat, int customer) {

		if (seat == null) {
			if (soldOut) {
				return null;
			}
			soldOut = true;
			System.out.println("Sorry, we are sold out!");
		} else {
			Ticket ticket = new Ticket(flightNo, officeId, seat, customer);
			System.out.println(ticket);
			this.log.addTicket(ticket);
			return ticket;
		}
		return null;
	}

	/**
	 * Lists all seats sold for this flight in order of purchase.
	 *
	 * @return list of seats sold
	 */
	//Get seat and add into the log
	public List<Seat> getSeatLog() {

		return this.log.seatLog;
	}

	/**
	 * Lists all tickets sold for this flight in order of printing.
	 *
	 * @return list of tickets sold
	 */
	//Get ticket and add into the log
	public List<Ticket> getTransactionLog() {

		return this.log.ticketLog;
	}

	static enum SeatClass {
		FIRST(0), BUSINESS(1), ECONOMY(2);

		private Integer intValue;

		private SeatClass(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	static enum SeatLetter {
		A(0), B(1), C(2), D(3), E(4), F(5);

		private Integer intValue;

		private SeatLetter(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	static enum SeatLetterFirstClass {
		A(0), B(1), E(4), F(5);

		private Integer intValue;

		private SeatLetterFirstClass(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	/**
	 * Represents a seat in the airplane FIRST Class: 1A, 1B, 1E, 1F ... BUSINESS
	 * Class: 2A, 2B, 2C, 2D, 2E, 2F ... ECONOMY Class: 3A, 3B, 3C, 3D, 3E, 3F ...
	 * (Row numbers for each class are subject to change)
	 */
	static class Seat {
		private SeatClass seatClass;
		private int row;
		private SeatLetter letter;

		public Seat(SeatClass seatClass, int row, SeatLetter letter) {
			this.seatClass = seatClass;
			this.row = row;
			this.letter = letter;
		}

		public SeatClass getSeatClass() {
			return seatClass;
		}

		public void setSeatClass(SeatClass seatClass) {
			this.seatClass = seatClass;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public SeatLetter getLetter() {
			return letter;
		}

		public void setLetter(SeatLetter letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return Integer.toString(row) + letter + " (" + seatClass.toString() + ")";
		}
	}

	/**
	 * Represents a flight ticket purchased by a customer
	 */
	static class Ticket {
		private String flightNo;
		private String officeId;
		private Seat seat;
		private int customer;
		public static final int TICKET_STRING_ROW_LENGTH = 31;

		public Ticket(String flightNo, String officeId, Seat seat, int customer) {
			this.flightNo = flightNo;
			this.officeId = officeId;
			this.seat = seat;
			this.customer = customer;
		}

		public int getCustomer() {
			return customer;
		}

		public void setCustomer(int customer) {
			this.customer = customer;
		}

		public String getOfficeId() {
			return officeId;
		}

		public void setOfficeId(String officeId) {
			this.officeId = officeId;
		}

		@Override
		public String toString() {
			String result, dashLine, flightLine, officeLine, seatLine, customerLine, eol;

			eol = System.getProperty("line.separator");

			dashLine = new String(new char[TICKET_STRING_ROW_LENGTH]).replace('\0', '-');

			flightLine = "| Flight Number: " + flightNo;
			for (int i = flightLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				flightLine += " ";
			}
			flightLine += "|";

			officeLine = "| Ticket Office ID: " + officeId;
			for (int i = officeLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				officeLine += " ";
			}
			officeLine += "|";

			seatLine = "| Seat: " + seat.toString();
			for (int i = seatLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				seatLine += " ";
			}
			seatLine += "|";

			customerLine = "| Client: " + customer;
			for (int i = customerLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				customerLine += " ";
			}
			customerLine += "|";

			result = dashLine + eol + flightLine + eol + officeLine + eol + seatLine + eol + customerLine + eol
					+ dashLine;

			return result;
		}
	}

	/**
	 * SalesLogs are security wrappers around an ArrayList of Seats and one of
	 * Tickets that cannot be altered, except for adding to them. getSeatLog returns
	 * a copy of the internal ArrayList of Seats. getTicketLog returns a copy of the
	 * internal ArrayList of Tickets.
	 */
	static class SalesLogs {
		private ArrayList<Seat> seatLog;
		private ArrayList<Ticket> ticketLog;

		private SalesLogs() {
			seatLog = new ArrayList<Seat>();
			ticketLog = new ArrayList<Ticket>();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Seat> getSeatLog() {
			return (ArrayList<Seat>) seatLog.clone();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Ticket> getTicketLog() {
			return (ArrayList<Ticket>) ticketLog.clone();
		}

		public void addSeat(Seat s) {
			seatLog.add(s);
		}

		public void addTicket(Ticket t) {
			ticketLog.add(t);
		}
	}

}
