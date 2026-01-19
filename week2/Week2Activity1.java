package week2;

public class Week2Activity1 {
    public static void main(String[] args) {
        int[] theaterRow = {0, 0, 0, 0, 0, 0, 0, 0};
        theaterRow[3] = 1;

        int seatAvailableCounter = 0;
        for (int i = 0; i < theaterRow.length; i++){
            if (theaterRow[i] == 0) {
                System.out.println("Available");
                seatAvailableCounter++;
            }
            else
                System.out.println("Not Available");
        }
        System.out.println("The total available seat is " + seatAvailableCounter);
        
    }
    
}
