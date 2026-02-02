import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("First Name: ");
            sb.append(sc.nextLine()).append("\n");
            System.out.print("Last Name: ");
            sb.append(sc.nextLine()).append("\n");
            System.out.print("Email: ");
            sb.append(sc.nextLine()).append("\n");
            System.out.print("Age: ");
            sb.append(sc.nextLine()).append("\n");
            System.out.print("Phone: ");
            sb.append(sc.nextLine()).append("\n");
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