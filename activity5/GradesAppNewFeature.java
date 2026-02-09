import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GradesAppNewFeature {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Use arraylist
        List<Subject> subject = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("grades.csv"));
            String line;
            br.readLine();
            int i = 0;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
            }
            for (int r = 0; r < 5; r++) {
                // Display menu
                System.out.println("""
                        Menu
                        [1] Add Grade for subject
                        [2] Display grades
                        [3] Exit
                        """);

                System.out.print("Choice: ");
                int choice = sc.nextInt();

                if (choice == 1) {
                    // Prompt user to enter subject
                    System.out.print("\nEnter subject: ");
                    String name = sc.next();

                    double p, m, f;
                    // Prompt user to enter its grades per subject
                    try {
                        System.out.print("Prelim: ");
                        p = sc.nextDouble();

                        System.out.print("Midterm: ");
                        m = sc.nextDouble();

                        System.out.print("Finals: ");
                        f = sc.nextDouble();

                        subject.add(name, p, f, m);
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Numbers from 1 to 100 only.");
                        sc.nextLine(); // clear bad input
                    }
                    System.out.println();

                } else if (choice == 2) {
                    System.out.println("\nSubject\tPrelim\tMidterm\tFinals\n");
                    for (int i = 0; i < subject.length; i++) {
                        // Print grades for subject and term
                        System.out.printf("%-12s", subjectName[i]);
                        for (int j = 0; j < 3; j++) {
                            System.out.printf("%-12.2f", gradeData[i][j]);
                        }
                        System.out.println();
                    }
                } else if (choice == 3) {
                    System.out.println("Good bye... Muwah!");
                    break;
                } else {
                    System.out.println("Invalid choice");
                    r--; // invalid choice won't count
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    }

    // for reading data
    public static void readData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("grades.csv"));
            String line;
            br.readLine();
            int i = 0;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Subject {
        String name;
        double prelim;
        double midterm;
        double finals;
    }
}
