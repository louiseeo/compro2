import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GradesApp {
    // Arrays to store subject names and its grades
    static String[] subjectName;
    static double[][] gradeData;

    public static void main(String[] args) {
        // Initialize arrays
        subjectName = new String[50];
        gradeData = new double[50][3];

        Scanner sc = new Scanner(System.in);

        for (int r = 0; r < 5; r++) {
            // Display menu
            System.out.println("""
                    Menu
                    [1] Add Grade for subject
                    [2] Exit
                    """);

            System.out.print("Choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                // Prompt user to enter subject
                System.out.print("\nEnter subject: ");
                subjectName[r] = sc.next();

                // Prompt user to enter its grades per subject
                System.out.print("Enter Prelim grade: ");
                try {
                    gradeData[r][0] = sc.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid number");
                    sc.nextLine(); // clear bad input
                }

                System.out.print("Enter Midterm grade: ");
                try {
                    gradeData[r][1] = sc.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid number");
                    sc.nextLine(); // clear bad input
                }

                System.out.print("Enter Finals grade: ");
                try {
                    gradeData[r][2] = sc.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid number");
                    sc.nextLine(); // clear bad input
                }

                System.out.println();
            } else if (choice == 2) {
                System.out.println("Good bye... Muwah!");
                break;
            } else {
                System.out.println("Invalid choice");
                r--; // invalid choice won't count
            }
        }

        sc.close(); // close scanner
    				
        writeData(); // call method
    }

    public static void writeData() {
        StringBuilder sb = new StringBuilder();

        // Add csv header row
        sb.append("Subject,Prelim,Midterm,Finals\n");

        for (int r = 0; r < subjectName.length; r++) {
            if (subjectName[r] == null) {
                break;
            }

            sb.append(subjectName[r]);
            for (int c = 0; c < gradeData[r].length; c++) {
                sb.append(",").append(gradeData[r][c]);
            }
            sb.append("\n");
        }

        // For writing file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("grade.csv"))) {
            bw.write(sb.toString());
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Print csv to program
        System.out.println(sb.toString());
    }
}