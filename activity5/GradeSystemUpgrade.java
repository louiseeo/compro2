import java.util.*;
import java.io.*;

// need to fix reading and writing and comments also edit mo pa yung csv
public class GradeSystemUpgrade {
    static ArrayList<Subject> subjects = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getValidChoice(0, 5);

            switch (choice) {
                case 1:
                    addSubject();
                    break;
                case 2:
                    displayAllGrades();
                    break;
                case 3:
                    searchSubject();
                    break;
                case 4:
                    editSubjectGrades();
                    break;
                case 0: {
                    System.out.println("Exiting program... Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;
                }
            }
        }
    }

    public static void displayMenu() {
        System.out.println("\n--- GRADE MENU ---");
        System.out.println("1. Add Subject / Enter Grades");
        System.out.println("2. Display All Grades");
        System.out.println("3. Search Subject");
        System.out.println("4. Edit Subject Grades");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    public static int getValidChoice(int min, int max) {
        int choice;
        while (true) {
            try {
                choice = sc.nextInt();
                sc.nextLine();
                if (choice >= min && choice <= max)
                    return choice;
                else
                    System.out.print("Invalid choice. Enter a number between " + min + " and " + max + ": ");
            } catch (Exception e) {
                System.out.print("Invalid input. Enter a number: ");
                sc.nextLine();
            }
        }
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
                    System.out.println("Grade must be 0-100.");
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.");
                sc.nextLine();
            }
        }
    }

    // --- Display All Grades ---
    public static void displayAllGrades() {
        if (subjects.isEmpty()) {
            System.out.println("No subjects available.");
            return;
        }
        System.out.printf("\n%-15s %-10s %-10s %-10s\n", "Subject", "Prelim", "Midterm", "Final");
        System.out.println("---------------------------------------------");
        for (Subject s : subjects) {
            System.out.printf("%-15s %-10.2f %-10.2f %-10.2f\n", s.name, s.prelim, s.midterm, s.finals);
        }
    }

    // --- Search Subject ---
    private static void searchSubject() {
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

    // --- Edit Subject Grades ---
    private static void editSubjectGrades() {
        if (subjects.isEmpty()) {
            System.out.println("No subjects available.");
            return;
        }
        System.out.print("Enter subject name to edit: ");
        String keyword = sc.nextLine().toLowerCase();

        for (Subject s : subjects) {
            if (s.name.toLowerCase().equals(keyword)) {
                System.out.println("Editing grades for " + s.name);
                s.prelim = getValidGrade("Prelim");
                s.midterm = getValidGrade("Midterm");
                s.finals = getValidGrade("Final");
                System.out.println("Grades updated successfully!");
                return;
            }
        }
        System.out.println("Subject not found.");
    }

    public static void saveData() {
        StringBuilder data = new StringBuilder("Subject,Prelim,Midterm,Final");
        for (Subject s : subjects) {
            data.append("\n")
                    .append(s.name).append(",")
                    .append(s.prelim).append(",")
                    .append(s.midterm).append(",")
                    .append(s.finals).append(",");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("grades.csv"))) {
            bw.write(data.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
