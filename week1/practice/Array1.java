import java.util.Scanner;

public class Array1 {
    public static void main(String args[]) {
        int[] array = { 1, 25, 7, 11, 9, 18, 12, 14, 15, 23 };
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

        Scanner input = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int num = input.nextInt();
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == num) {
                index = i;
                break;
            }
        }
        System.out.println("Index of number is " + index);
    }
}
