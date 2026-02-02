import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(System.in)) {
            sb.append("First Name: ");
            System.out.print("Enter First Name: ");

            sb.append(sc.nextLine()).append("\nLast Name: ");
            System.out.print("Enter Last Name: ");

            sb.append(sc.nextLine()).append("\nEmail:");
            System.out.print("Enter Email: ");

            sb.append(sc.nextLine()).append("\nAge: ");
            System.out.print("Enter Age: ");

            sb.append(sc.nextLine()).append("\nPhone: ");
            System.out.print("Enter Phone: ");
            sb.append(sc.nextLine());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
        }

        // try-with-resource
        try (FileWriter fw = new FileWriter("data.txt")) {
            fw.write(sb.toString());
            System.out.println("Data is saved...");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}