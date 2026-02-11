import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GradesApp {
    // For counting of subjects
    static int count = 0; 
    // Arrays to store subject names and its grades
    static String[] subjectName;
    static double[][] gradeData;

    public static void main(String[] args) {
        // Initialize arrays
        subjectName = new String[50];
        gradeData = new double[50][3];

        Scanner sc = new Scanner(System.in);

        while (true) {
            // Display menu
            System.out.println("""
                    Menu
                    [1] Add Grade for subject
                    [2] Exit
                    """);

            int c = 0;
            // Add validation for choice
            while (true) {
                System.out.print("Choice: ");
                try {
                    c = sc.nextInt();
                    if (c == 1 || c == 2) {
                        break; // exit loop if valid input
                    } else {
                        System.out.println("Invalid choice. Enter 1 or 2.\n");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid choice. Enter 1 or 2.\n");
                    sc.nextLine();
                }
            }

            if (c == 1) {
                // Stops if max subjects is entered
                if (count == 50) {
                    System.out.println("Maximum number of subjects is reached.");
                    break;
                }

                // Prompt user to enter subject
                System.out.print("\nEnter subject: ");
                subjectName[count] = sc.next();

                // Prompt user to enter prelim grade
                double p = 0;
                while (true) {
                    System.out.print("Prelim: ");
                    try {
                        p = sc.nextDouble();
                        if (p >= 1 && p <= 100) {
                            gradeData[count][0] = p;
                            break; // escape if user entered a valid input
                        } else
                            System.out.println("Invalid input. Enter from 1 to 100 only.\n");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Enter from 1 to 100 only.\n");
                        sc.nextLine();
                    }
                }

                // Prompt user to enter midterm grade
                double m = 0;
                while (true) {
                    System.out.print("Midterm: ");
                    try {
                        m = sc.nextDouble();
                        if (m >= 1 && m <= 100) {
                            gradeData[count][1] = m;
                            break; // escape if user entered a valid input
                        } else
                            System.out.println("Invalid input. Enter from 1 to 100 only.\n");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Enter from 1 to 100 only.\n");
                        sc.nextLine();
                    }
                }

                // Prompt user to enter final grade
                double f = 0;
                while (true) {
                    System.out.print("Final: ");
                    try {
                        f = sc.nextDouble();
                        if (f >= 1 && f <= 100) {
                            gradeData[count][2] = f;
                            break; // escape if user entered a valid input
                        } else
                            System.out.println("Invalid input. Enter from 1 to 100 only.\n");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Enter from 1 to 100 only.\n");
                        sc.nextLine();
                    }
                }
                System.out.println();

                count++; // increments number of subjects entered
            } else if (c == 2) {
                System.out.println("Good bye... Muwah!\n");
                break;
            } 
        }

        sc.close(); // close scanner
        writeData(); // call method
    }

    public static void writeData() {
        StringBuilder sb = new StringBuilder();

        // Add csv header row
        sb.append("Subject,Prelim,Midterm,Finals\n");

        for (int r = 0; r < count; r++) {
            sb.append(subjectName[r]);
            for (int c = 0; c < gradeData[r].length; c++) {
                sb.append(",").append(gradeData[r][c]);
            }
            sb.append("\n");
        }

        // For writing file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("grades.csv"))) {
            bw.write(sb.toString());
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Print csv to program
        System.out.println(sb.toString());
    }
}