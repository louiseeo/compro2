package com.louiseeo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * MultiThreadedListApp is a multithreaded Grade Management System.
 * It allows users to view, search, enter, and edit subject grades.
 * A background saver thread automatically saves changes to a JSON file,
 * while a background fetcher thread periodically reloads data from the file.
 */
public class MultiThreadedListApp {
    /** List that holds all grade entries. */
    static List<Grade> data = new CopyOnWriteArrayList<>();
    /** Path where grades are loaded and saved. */
    static final String FILE_PATH = "data/grades.json";
    /** Flag that indicates if unsaved changes exist. */
    static boolean hasChanges = false;
    /** Gson instance for printing human readable JSON output */
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    /** Scanner that is accessible for the whole class */
    static Scanner sc = new Scanner(System.in);

    /**
     * Entry point of the application.
     * Loads existing data, starts the saver and fetcher daemon threads,
     * then launches the main menu.
     *
     * @param args
     */
    public static void main(String[] args) {
        readFile();

        Thread saver = new Thread(() -> {
            while (true) {
                if (hasChanges) {
                    saveToDisk();
                    hasChanges = false;
                    System.out.println("\n[Auto-saved to file]");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread fetcher = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    readFile();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        saver.setDaemon(true);
        fetcher.setDaemon(true);
        saver.start();
        fetcher.start();

        gradeMenu();
    }

    /**
     * Displays the main menu where user navigates.
     * Loops until the user chooses to exit (option 0).
     */
    public static void gradeMenu() {
        int choice = -1;

        while (choice != 0) {
            System.out.println("""
                    \nWelcome to Grade Program!
                        Menu:
                        [1] View Grades
                        [2] Search Grade
                        [3] Enter Grade
                        [4] Edit Grade
                        [0] Exit
                        """);

            System.out.print("Choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice! Pick from 0 to 4 only.");
                continue;
            }

            switch (choice) {
                case 1 -> viewGrades();
                case 2 -> searchGrade(sc);
                case 3 -> enterGrade(sc);
                case 4 -> editGrade(sc);
                case 0 -> {
                    saveToDisk();
                    System.out.println("Thank you for using Grade Program! Goodbye!");
                }
                default -> System.out.println("Invalid choice! Pick from 0 to 4 only.");
            }
        }
    }

    /**
     * Displays all saved grades in a formatted table.
     * Shows a message if no grades have been saved yet.
     */
    public static void viewGrades() {
        if (data.isEmpty()) {
            System.out.println("No grades saved yet.");
            return;
        }
        String border = "----------------------------------------------------------";
        System.out.println(border);
        System.out.printf("| %-3s | %-15s | %-8s | %-8s | %-8s |\n", "#", "Subject", "Prelim", "Midterm", "Final");
        System.out.println(border);
        for (int i = 0; i < data.size(); i++) {
            Grade g = data.get(i);
            System.out.printf("| %-3d | %-15s | %-8.2f | %-8.2f | %-8.2f |\n", i + 1, g.getSubject(), g.getPrelim(),
                    g.getMidterm(), g.getFinalGrade());
        }
        System.out.println(border);
    }

    /**
     * Searches for grades by subject keyword and displays matching results.
     * Prints a message if no match is found.
     *
     * @param sc the Scanner used to read user input
     */
    public static void searchGrade(Scanner sc) {
        System.out.print("Enter subject to search: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        List<Grade> results = new ArrayList<>();
        for (Grade g : data) {
            if (g.getSubject().toLowerCase().contains(keyword)) {
                results.add(g);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No subject match found.");
            return;
        }

        String border = "---------------------------------------------------";
        System.out.println(border);
        System.out.printf("| %-15s | %-8s | %-8s | %-8s |\n", "Subject", "Prelim", "Midterm", "Final");
        System.out.println(border);
        for (Grade g : results) {
            System.out.printf("| %-15s | %-8.2f | %-8.2f | %-8.2f |\n", g.getSubject(), g.getPrelim(), g.getMidterm(),
                    g.getFinalGrade());
        }
        System.out.println(border);
    }

    /**
     * Prompts the user to enter a new subject and its grades,
     * then adds the entry to the data list.
     *
     * @param sc the Scanner used to read user input
     */
    public static void enterGrade(Scanner sc) {
        System.out.print("Enter subject: ");
        String subject = sc.nextLine().trim();
        if (subject.isEmpty()) {
            System.out.println("Subject name cannot be empty!");
            return;
        }
        double prelim = getValidGrade("Prelim");
        double midterm = getValidGrade("Midterm");
        double finalGrade = getValidGrade("Final");
        data.add(new Grade(subject, prelim, midterm, finalGrade));
        hasChanges = true;
        System.out.println("Grade successfully added!");
    }

    /**
     * Displays the grade list and allows the user to edit an existing entry.
     * Loops until a valid index and valid input are entered.
     *
     * @param sc the Scanner used to read user input
     */
    public static void editGrade(Scanner sc) {
        viewGrades();
        if (data.isEmpty())
            return;

        while (true) {
            System.out.print("Enter number to edit (1-" + data.size() + "): ");
            try {
                int index = Integer.parseInt(sc.nextLine().trim()) - 1;
                if (index < 0 || index >= data.size()) {
                    System.out.println("Invalid number! Choose again.\n");
                    continue;
                }
                System.out.print("Enter new subject: ");
                String subject = sc.nextLine().trim();
                if (subject.isEmpty()) {
                    System.out.println("Subject name cannot be empty!\n");
                    continue;
                }
                double prelim = getValidGrade("Prelim");
                double midterm = getValidGrade("Midterm");
                double finalGrade = getValidGrade("Final");
                data.set(index, new Grade(subject, prelim, midterm, finalGrade));
                hasChanges = true;
                System.out.println("Grade successfully updated!");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Choose again.\n");
            }
        }
    }

    /**
     * Repeatedly prompts the user for a grade value until a valid number
     * between 0 and 100 is entered.
     *
     * @param term the label for the grade period (e.g., "Prelim", "Midterm",
     *             "Final")
     * @return a valid grade value between 0.0 and 100.0
     */
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
                    System.out.println("Grade must be from 0-100!\n");
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.\n");
                sc.nextLine();
            }
        }
    }

    /**
     * Saves the current grade list to a JSON file using Gson.
     */
    public static void saveToDisk() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    /**
     * Reads grade data from the JSON file and loads it into the data list.
     * If the file does not exist, the method returns without doing anything.
     */
    public static void readFile() {
        File file = new File(FILE_PATH);
        if (!file.exists())
            return;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            Type type = new TypeToken<List<Grade>>() {
            }.getType();
            List<Grade> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                data.clear();
                data.addAll(loaded);
            }
        } catch (IOException e) {
            System.out.println("Error reading: " + e.getMessage());
        }
    }
}