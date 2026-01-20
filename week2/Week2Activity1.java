package week2;
import java.util.*;

public class Week2Activity1 {
    public static void main(String[] args) {
        int[] theaterRow = { 0, 0, 0, 0, 0, 0, 0, 0 };
        theaterRow[3] = 1;

        int seatAvailableCounter = 0;
        for (int i = 0; i < theaterRow.length; i++) {
            if (theaterRow[i] == 0) {
                System.out.println("Available");
                seatAvailableCounter++;
            } else
                System.out.println("Not Available");
        }
        System.out.println("The total available seat is " + seatAvailableCounter);

        // 1. Declare and initialize the 2D array for the theater
        int[][] theater = new int[5][8]; // 5 rows, 8 columns

        // 2. Book the seat at row 2, column 5
        theater[1][4] = 1;

        // 3. Book the seat at row 0, column 0
        theater[0][0] = 1;

        System.out.println("Theater Seating Chart (|-| = Available, |x| = Booked):");
        // 4. Use nested loops to print the seating chart
        // The outer loop should iterate through rows
        // The inner loop should iterate through columns
        // TODO: Your code here
        int seatBookCount = 0;
        String theateret = "";
        for (int i = 0; i < theater.length; i++) {
            for (int j = 0; j < theater[i].length; j++) {
                if (theater[i][j] == 1) {
                    theateret = "|x|";
                    seatBookCount++;
                } else
                    theateret = "|-|";

                System.out.print(theateret + " ");
            }

            System.out.println();
        }

        // 5. Count and print the total number of booked seats
        // TODO: Your code here
        System.out.println("The total number of booked seats are: " + seatBookCount);

        Scanner input = new Scanner(System.in);
        System.out.println("Book seat? ");
        String answer = input.nextLine();

        
    }
}
