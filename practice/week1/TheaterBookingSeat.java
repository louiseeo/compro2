import java.util.Scanner;
  public class TheaterBookingSeat{
// EXTRA ACTIVITY(not included in part 3 instructions)
        Scanner input = new Scanner(System.in);
        String answer;

        do {
            while (true) {
                System.out.print("\nBook a seat? (Y/N): ");
                answer = input.nextLine();

                if (answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("N")) {
                    break; // valid input
                } else {
                    System.out.println("Invalid input! Please enter only Y or N.");
                }
            }

            // Only book if user entered Y
            if (answer.equalsIgnoreCase("Y")) {
                // Count available seats
                int availableCount = 0;
                for (int i = 0; i < theater.length; i++) {
                    for (int j = 0; j < theater[i].length; j++) {
                        if (theater[i][j] == 0) {
                            availableCount++;
                        }
                    }
                }

                if (availableCount > 0) {
                    // Pick a random available seat
                    int randomPick = (int)(Math.random() * availableCount);
                    int counter = 0;
                    boolean booked = false;

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

                    System.out.println("Seat booked successfully!");
                } else {
                    System.out.println("No available seats to book!");
                }

                // Print updated seating chart
                System.out.println("\nUpdated Theater Seating Chart:");
                for (int i = 0; i < theater.length; i++) {
                    for (int j = 0; j < theater[i].length; j++) {
                        System.out.print(theater[i][j] == 1 ? "|x| " : "|-| ");
                    }
                    System.out.println();
                }
                System.out.println("The total number of booked seats are: " + seatBookCount);
            }

        } while (answer.equalsIgnoreCase("Y"));

        System.out.println("Booking finished... Goodbye!");
        input.close(); // Close scanner
    }
}
}
