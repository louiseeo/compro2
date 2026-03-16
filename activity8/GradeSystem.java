package com.louiseeo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.louiseeo.model.Grades;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.Type;

/**
 * Grade management system with JSON.
 * Enter, display, auto-save/load grades for user-given subjects.
 */
public class GradeSystem {
    // Initialize static fields
    static List<Grades> gradeList = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    /**
     * Main menu: [1] Enter [2] Display [0] Exit+Save
     */
    public static void main(String[] args) {
        // load .json file
        gradeList = loadGrades();

        // Start main menu loop
        while (true) {
            System.out.println("""
                    \nMAIN MENU:
                    [1] Enter Grades
                    [2] Display Grades
                    [0] Exit
                    """);

            System.out.print("Enter choice: ");
            int choice;

            // Validate the choice
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter 0, 1, or 2 only.");
                sc.nextLine();
                continue;
            }

            // Switch statement for main menu choices
            switch (choice) {
                case 1:
                    enterGrades(); // calls the method for choice 1
                    break;
                case 2:
                    displayGrades(); // calls method for choice 2
                    break;
                case 0:
                    saveGrades(); // auto-save grades to json
                    System.out.println("Exiting program... Goodbye!");
                    sc.close();
                    System.exit(0); // stops the program
                    break;
                default: // if user entered an invalid choice
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }

    /**
     * Allows the user to enter grades for new subjects.
     * For each subject, prompts for Prelim, Midterm, and Final grades.
     * Validates the nested menu to only enter 1 or 0
     * User can enter multiple subjects or return to the main menu.
     */
    private static void enterGrades() {
        while (true) {
            System.out.println("""
                    [1] Enter subject
                    [0] Return to menu
                    """);
            System.out.print("Choice: ");

            int c;
            try {
                c = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Choose 1 or 0.\n");
                sc.nextLine(); // clear invalid input
                continue;
            }

            switch (c) {
                case 1: {
                    System.out.print("Enter name of subject: ");
                    sc.nextLine();
                    String subject = sc.nextLine();

                    double prelim = checkGrades("Prelim");
                    double midterm = checkGrades("Midterm");
                    double finals = checkGrades("Final");

                    gradeList.add(new Grades(subject, prelim, midterm, finals));
                    System.out.println("Subject added successfully!\n");
                }
                    break;
                case 0: {
                    return; // go back to main menu
                }
                default:
                    System.out.println("Invalid input! Choose 1 or 0.\n");
                    break;
            }
        }
    }

    /**
     * Validates input to ensure it is a number between 1 and 100.
     *
     * @param term the term name (e.g., "Prelim", "Midterm", "Final")
     * @return the validated grade as a double
     */

    private static double checkGrades(String term) {
        double grade;
        while (true) {
            System.out.print(term + ": ");
            try {
                grade = sc.nextDouble();
                if (grade >= 0 && grade <= 100) {
                    return grade;
                } else {
                    System.out.println("Invalid grade! Enter 1-100 only.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Enter a number from 1 to 100 only.");
                sc.nextLine();
            }
        }
    }

    /**
     * Displays all entered grades in a formatted table.
     * If no grades exist, displays a message indicating no grades are available.
     */
    private static void displayGrades() {
        if (gradeList.isEmpty()) {
            System.out.println("No grades available yet.");
            return;
        }

        System.out.println("\n                  GRADE TABLE");
        System.out.println("------------------------------------------------");
        System.out.printf("%-10s %-11s %-11s %-11s\n",
                "Subject", "Prelim", "Midterm", "Final");
        System.out.println("------------------------------------------------");

        for (Grades g : gradeList) {
            System.out.printf("%-10s %-11.2f %-11.2f %-11.2f\n", g.getSubject(), g.getPrelim(), g.getMidterm(),
                    g.getFinals());
        }

        System.out.println("------------------------------------------------");
    }

    /**
     * Saves all grades to JSON file.
     * File path: "activity8/demo/data/grades.json"
     * Uses Gson with pretty printing.
     * 
     * @throws IOException if file write fails
     */

    public static void saveGrades() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try (FileWriter fw = new FileWriter("activity8/demo/data/grades.json")) {
            gson.toJson(gradeList, fw);
            System.out.println("Grades saved to JSON!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads grades from a JSON file ("activity8/demo/data/grades.json").
     * 
     * @return List<Grades> containing the loaded grades
     * @throws IOException if file read fails
     */
    public static List<Grades> loadGrades() {
        List<Grades> gradeList = new ArrayList<>();
        try (FileReader fr = new FileReader("activity8/demo/data/grades.json")) {
            Gson gson = new Gson();
            Type gradesListType = new TypeToken<ArrayList<Grades>>() {
            }.getType();
            List<Grades> gr = gson.fromJson(fr, gradesListType);
            if (gr != null)
                gradeList = gr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gradeList;
    }
}