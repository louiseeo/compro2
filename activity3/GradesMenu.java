import java.util.Scanner;

public class GradesMenu {
    public static void main(String[] args) {

        // Declare arrays for subjects, terms, and grades  
        double[][] grades = new double[3][3];  
        String[] subjects = {"COMPRO2", "DSA", "OOP"};  
        String[] terms = {"Prelim", "Midterm", "Final"};  

        Scanner input = new Scanner(System.in);  

        int choice = 0; // Initialize choice  

        // Start MAIN MENU loop  
        while (choice != 3) {  
            System.out.println("""
                    \nMAIN MENU:
                    [1] Enter Grades
                    [2] Display Grades
                    [3] Exit
                    """);  

            // Ask user for main menu choice  
            System.out.print("Enter choice: ");  
            choice = input.nextInt();  

            // Choice 1: Enter grades for chosen subject
            if (choice == 1) {  

                while (true) {  
                    System.out.println("""
                            \nEnter grade for:
                            [1] COMPRO2
                            [2] DSA
                            [3] OOP
                            [0] Go back
                            """);  

                    System.out.print("Enter choice: ");  
                    int subChoice = input.nextInt();  

                    if (subChoice == 1) {  
                        System.out.println("\nEnter grades for " + subjects[0]);  
                        for (int j = 0; j < 3; j++) {  
                            while (true) {
                                System.out.print(terms[j] + ": ");
                                if (input.hasNextDouble()) { // Checks if user entered correct number
                                    double grade = input.nextDouble();
                                    if (grade >= 0 && grade <= 100) {
                                        grades[0][j] = grade; // Save valid grade
                                        break; // Moves to next term
                                    } else {
                                        System.out.println("Invalid grade! Enter 0–100 only.");
                                    }
                                } else {
                                    System.out.println("Invalid input! Enter a number.");
                                    input.next();
                                }
                            }
                        }  
                        System.out.println("Grades saved...");  
                    }   
                    else if (subChoice == 2) {  
                        System.out.println("\nEnter grades for " + subjects[1]);  
                        for (int j = 0; j < 3; j++) {  
                            while (true) {
                                System.out.print(terms[j] + ": ");
                                if (input.hasNextDouble()) {
                                    double grade = input.nextDouble();
                                    if (grade >= 0 && grade <= 100) {
                                        grades[1][j] = grade;
                                        break;
                                    } else {
                                        System.out.println("Invalid grade! Enter 0–100 only.");
                                    }
                                } else {
                                    System.out.println("Invalid input! Enter a number.");
                                    input.next();
                                }
                            }
                        }  
                        System.out.println("Grades saved...");  
                    }   
                    else if (subChoice == 3) {  
                        System.out.println("\nEnter grades for " + subjects[2]);  
                        for (int j = 0; j < 3; j++) {  
                            while (true) {
                                System.out.print(terms[j] + ": ");
                                if (input.hasNextDouble()) {
                                    double grade = input.nextDouble();
                                    if (grade >= 0 && grade <= 100) {
                                        grades[2][j] = grade;
                                        break;
                                    } else {
                                        System.out.println("Invalid grade! Enter 0–100 only.");
                                    }
                                } else {
                                    System.out.println("Invalid input! Enter a number.");
                                    input.next();
                                }
                            }
                        }  
                        System.out.println("Grades saved...");  
                    }   
                    else if (subChoice == 0) {  
                        break; // Go back to main menu
                    }   
                    else {  
                        System.out.println("Invalid subject choice."); // For invalid inputs  
                    }  
                }  
            } 
            // Choice 2: Display Grades  
            else if (choice == 2) {  
                // Print table header  
                System.out.println("\n                  GRADE TABLE");  
                System.out.println("------------------------------------------------");  
                System.out.printf("%-11s %-11s %-11s %-11s\n",  
                        "Subject", "Prelim", "Midterm", "Final");  
                System.out.println("------------------------------------------------");  

                for (int i = 0; i < subjects.length; i++) {  
                    // Print grades for subject and term  
                    System.out.printf("%-12s", subjects[i]);  
                    for (int j = 0; j < 3; j++) {  
                        System.out.printf("%-12.2f", grades[i][j]);  
                    }  
                    System.out.println();  
                }  
                System.out.println("------------------------------------------------");  
            }  
            // Choice 3: Exit program  
            else if (choice == 3) {  
                System.out.println("Exiting program... Goodbye!");  
            }   
            else {  
                System.out.println("Invalid choice!"); // Prints if user enters invalid choice  
            }  
        }  

        input.close(); // Close scanner  
    }
}
