package com.louiseeo;

import java.util.Scanner;

public class GradeSystem {
    // Declare arrays for subjects, terms, and grades
    static double[][] grades = new double[3][3];
    static String[] subjects = {"COMPRO2", "DSA", "OOP"};
    static String[] terms = {"Prelim", "Midterm", "Final"};
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Start main menu loop
        while (true) {
            System.out.println("""
                    \nMAIN MENU:
                    [1] Enter Grades
                    [2] Display Grades
                    [3] Exit
                    """);

            System.out.print("Enter choice: ");
            int choice = input.nextInt();

            // Switch statement for main menu choices
            switch (choice) {
                case 1:
                    enterGrades(); // calls the method for choice 1
                    break;
                case 2:
                    displayGrades(); // calls method for choice 2
                    break;
                case 3:
                    System.out.println("Exiting program... Goodbye!");
                    input.close();
                    System.exit(0); // stops the program
                    break;
                default: // if user entered  an invalid choice
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }

    // Method for entering grades
    private static void enterGrades() {
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

            // switch statement for the sub-choice
            switch (subChoice) {
                case 1:
                    enterGradesForSubject(0);
                    break;
                case 2:
                    enterGradesForSubject(1);
                    break;
                case 3:
                    enterGradesForSubject(2);
                    break;
                case 0:
                    System.out.println("Returning to Main Menu...");
                    return; // exits sub-menu
                default:
                    System.out.println("Invalid subject choice. Try again.");
                    break;
            }
        }
    }

    // Helper method for entering grades for a specific subject terms
    private static void enterGradesForSubject(int subjectIndex) {
        System.out.println("\nEnter grades for " + subjects[subjectIndex]);
        for (int j = 0; j < terms.length; j++) {
            while (true) {
                System.out.print(terms[j] + ": ");
                if (input.hasNextDouble()) {
                    double grade = input.nextDouble();
                    if (grade >= 0 && grade <= 100) {
                        grades[subjectIndex][j] = grade; // save valid grade
                        break; // move to next term
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

    // Method for displaying grades
    private static void displayGrades() {
        System.out.println("\n                  GRADE TABLE");
        System.out.println("------------------------------------------------");
        System.out.printf("%-11s %-11s %-11s %-11s\n",
                "Subject", "Prelim", "Midterm", "Final");
        System.out.println("------------------------------------------------");

        for (int i = 0; i < subjects.length; i++) {
            System.out.printf("%-12s", subjects[i]);
            for (int j = 0; j < terms.length; j++) {
                System.out.printf("%-12.2f", grades[i][j]);
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------");
    }
}
