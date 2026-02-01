package activity1;

import java.util.*;

public class Week2Activity1 {
    public static void main(String[] args) {
        // 1. Declare and initialize the 1D array for a single row
        int[] theaterRow = new int[8];

        // 2. Book the seat at index 3 (the 4th seat)
        theaterRow[3] = 1; 
        
        System.out.println("Seat Status (0=Available, 1=Booked):");
        
        // 3. Loop through the array and print each seat's status
        int seatAvailableCounter = 0;
        for (int i = 0; i < theaterRow.length; i++) {
            if (theaterRow[i] == 0) {
                System.out.println("Available");
                seatAvailableCounter++;
            } else
                System.out.println("Not Available");
        }

        // 4. Count and print the number of available seats
        System.out.println("The total number of available seat is " + seatAvailableCounter);
        
        // PART 3: 2d Theater Seating

        // 1. Declare and initialize the 2D array for the theater
        int[][] theater = new int[5][8]; // 5 rows, 8 columns

        // 2. Book the seat at row 2, column 5
        theater[1][4] = 1;

        // 3. Book the seat at row 0, column 0
        theater[0][0] = 1;

        System.out.println("\nTheater Seating Chart (|-| = Available, |x| = Booked):");
        // 4. Use nested loops to print the seating chart
        // The outer loop should iterate through rows
        // The inner loop should iterate through columns
        int seatBookCount = 0; // initialize for counting of available seats
        String seat = " ";
        for (int i = 0; i < theater.length; i++) {
            for (int j = 0; j < theater[i].length; j++) {
                 if (theater[i][j] == 1){
                    System.out.print("|x| ");
                    seatBookCount++;
                }  else
                    System.out.print("|-| ");
            }
            System.out.println();
        }

        // 5. Count and print the total number of booked seats
        System.out.println("The total number of booked seats are: " + seatBookCount);

        // EXTRA ACTIVITY(not included in part 3 instructions)
        Scanner input = new Scanner(System.in);
        System.out.print("\nBook seat? (Y/N): ");
        String answer = input.nextLine();

        boolean booked = false;
        if (answer.equals("Y") || answer.equals("y")) {
            
            int availableCount = 0;
            for (int i = 0; i < theater.length; i++) {
                for (int j = 0; j < theater[i].length; j++) {
                    if (theater[i][j] == 0) {
                        availableCount++;
                    }
                }
            }

            if (availableCount > 0) {
                int randomPick = (int) (Math.random() * availableCount);
                int counter = 0;

                for (int i = 0; i < theater.length; i++) {
                    for (int j = 0; j < theater[i].length; j++) {
                        if (theater[i][j] == 0) {
                            if (counter == randomPick) {
                                theater[i][j] = 1;
                                booked = true;
                                seatBookCount++;
                                break; // stop inner loop
                            }
                            counter++;
                        }
                    }
                    if (booked)
                        break; // stop outer loop
                }
            }
            System.out.println("Seat booked successfully!");
        } 
        // print updated seating chart
        for (int i = 0; i < theater.length; i++) {
            for (int j = 0; j < theater[i].length; j++) {
                if (theater[i][j] == 1)
                    System.out.print("|x| ");
                else
                    System.out.print("|-| ");
            }
            System.out.println();
        }
        System.out.println("The total number of booked seats are: " + seatBookCount);
    }
}