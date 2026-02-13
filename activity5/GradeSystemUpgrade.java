import java.util.*;
import java.io.*;

public class GradeSystemUpgrade {
    static ArrayList<Subject> subjects = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadData(); // read the existing CSV at start

        while (true) {
            displayMenu();

            int choice;
            try {
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // clear buffer
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Choose a number from 0–3.");
                sc.nextLine(); // clear invalid input
                continue; // go back to menu
            }

            switch (choice) {
                case 1:
                    addSubject(); // calls addSubject method
                    break;
                case 2:
                    displayAllGrades(); // calls the method to display grades
                    break;
                case 3:
                    searchSubject(); // searches existing subject saved
                    break;
                case 0:
                    System.out.println("Exiting program... Goodbye!\n");
                    writeData(); // save before exiting
                    sc.close();
                    System.exit(0); // stops program
                    break;
                default:
                    System.out.println("Invalid choice. Choose a number from 0–3.");
            }
        }
    }

    public static void displayMenu() {
        System.out.println("""
            \nWelcome to Grade Program!
                Menu
                [1] Add Grade for Subject
                [2] Display grades
                [3] Search
                [0] Exit
            """);
    }

    // Add subject
    public static void addSubject() {
        Subject s = new Subject();
        System.out.print("Enter subject name: ");
        s.name = sc.nextLine();

        s.prelim = getValidGrade("Prelim");
        s.midterm = getValidGrade("Midterm");
        s.finals = getValidGrade("Final");

        subjects.add(s);
        System.out.println("Subject added successfully!");
    }

    // Method that checks if entered grade is valid
    public static double getValidGrade(String term) {
        double grade;
        while (true) {
            System.out.print(term + ": ");
            try {
                grade = sc.nextDouble();
                sc.nextLine();
                if (grade >= 0 && grade <= 100)
                    return grade;
                else
                    System.out.println("Grade must be 0–100.");
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.");
                sc.nextLine();
            }
        }
    }

    // Display grades method
    public static void displayAllGrades() {
        if (subjects.isEmpty()) { // checks if there are subjects saved
            System.out.println("No subjects available.");
            return;
        }
        System.out.printf("\n%-15s %-10s %-10s %-10s\n", "Subject", "Prelim", "Midterm", "Final");
        System.out.println("---------------------------------------------");
        for (Subject s : subjects) {
            System.out.printf("%-15s %-10.2f %-10.2f %-10.2f\n", s.name, s.prelim, s.midterm, s.finals);
        }
    }

    // Search method
    public static void searchSubject() {
        if (subjects.isEmpty()) {
            System.out.println("No subjects available.");
            return;
        }
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;

        System.out.printf("\n%-15s %-10s %-10s %-10s\n", "Subject", "Prelim", "Midterm", "Final");
        System.out.println("---------------------------------------------");

        for (Subject s : subjects) {
            if (s.name.toLowerCase().contains(keyword)) {
                System.out.printf("%-15s %-10.2f %-10.2f %-10.2f\n", s.name, s.prelim, s.midterm, s.finals);
                found = true;
            }
        }
        if (!found)
            System.out.println("No subjects found with that keyword.");
    }

    public static void writeData() {
        StringBuilder sb = new StringBuilder("Subject,Prelim,Midterm,Final");
        for (Subject s : subjects) {
            sb.append("\n")
                    .append(s.name).append(",")
                    .append(s.prelim).append(",")
                    .append(s.midterm).append(",")
                    .append(s.finals);
        }

        // writing the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("grades.csv"))) {
            bw.write(sb.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print csv to program
        System.out.println(sb.toString());
    }

    public static void loadData() {
        subjects.clear();
        try (Scanner fileScanner = new Scanner(new java.io.File("grades.csv"))) {
            if (fileScanner.hasNextLine())
                fileScanner.nextLine(); // skip the header
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Subject s = new Subject();
                    s.name = parts[0];
                    s.prelim = Double.parseDouble(parts[1]);
                    s.midterm = Double.parseDouble(parts[2]);
                    s.finals = Double.parseDouble(parts[3]);
                    subjects.add(s);
                }
            }
        } catch (Exception e) {
            System.out.println("No existing data found, starting fresh.");
        }
    }
}

// Subject class
class Subject {
    String name;
    double prelim;
    double midterm;
    double finals;
}
