package practice.week3;

import java.util.Scanner;

public class ExceptionPractice1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a number: ");
        try {
            int number = input.nextInt();
        } catch (Exception e) {
            System.out.println("Mali ka gurlll");

            System.out.print("\nEnter a number: ");
            int number = inputNumber();

        }
    }

    public static int inputNumber() {
        int number = 0;
        Scanner input = new Scanner(System.in);

        while (true) {
            try {
                number = input.nextInt();
                return number;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("Engkk, ulet!!");
                System.out.print("\nEnter a new number: ");

            }
        }

    }

}
