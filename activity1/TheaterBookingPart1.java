import java.util.Scanner;

public class TheaterBookingPart1 {
	public static void main(String[] args) {
		System.out.println("PART 1: Single Row");
		// 1. Declare and initialize the 1D array for a single row
		int[] theaterRow = new int[8];

		// 2. Book the seat at index 3 (the 4th seat)
		theaterRow[3] = 1;

		System.out.println("\nSeat Status (0=Available, 1=Booked):");

		// 3. Loop through the array and print each seat's status
		int seatAvailableCounter = 0;
		for (int i = 0; i < theaterRow.length; i++) {
			if (theaterRow[i] == 0) {
				System.out.println("Seat " + (i + 1) + ": " + "Available");
				seatAvailableCounter++;
			} else {
				System.out.println("Seat " + (i + 1) + ": " + "Not Available");
			}
		}

		// 4. Count and print the number of available seats
		System.out.println("The total number of available seat is " + seatAvailableCounter);
	}
}
