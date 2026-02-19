public class TheaterBookingPart2 {
    public static void main(String[] args) {
        // 1. Declare and initialize the 2D array for the theater
        int[][] theater = new int[5][8]; // 5 rows, 8 columns

        // 2. Book the seat at row 2, column 5
        theater[1][4] = 1;

        // 3. Book the seat at row 0, column 0
        theater[0][0] = 1;

        System.out.println("\nTheater Seating Chart (|-| = Available, |x| = Booked):");
        // 4. Use nested loops to print the seating chart
        int seatBookCount = 0; // initialize for counting of available seats
        String seat = " ";
        for (int i = 0; i < theater.length; i++) {
            for (int j = 0; j < theater[i].length; j++) {
                if (theater[i][j] == 1) {
                    System.out.print("|x| ");
                    seatBookCount++;
                } else {
                    System.out.print("|-| ");
                }
            }
            System.out.println();
        }

        // 5. Count and print the total number of booked seats
        System.out.println("The total number of booked seats are: " + seatBookCount);
