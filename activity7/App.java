package com.phonebook;

import com.phonebook.models.Contact;
import com.phonebook.services.PhonebookService;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PhonebookService pb = new PhonebookService(); // instantiate PhoneboookService
        pb.loadFromCSV("contacts.csv"); // call load method

        while (true) {
            // Create a menu
            System.out.println("""
                    Welcome to Phonebook Menu!
                    1. Add
                    2. Search
                    3. Remove
                    4. Display All
                    5. Save to CSV
                    0. Exit
                    """);
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            // Cases
            switch (choice) {
                case 1:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter phone number: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    Contact c = new Contact(name, phone, email);
                    pb.addContact(c); // adds contact to hashmap
                    System.out.println("Contact successfully added!\n");
                    break;
                case 2:
                    System.out.print("Enter name to search: ");
                    String searchName = sc.nextLine();
                    boolean found = pb.searchContact(searchName);
                    if (found) {
                        System.out.println("The contact exist.\n");
                    } else {
                        System.out.println("Contact not found\n");
                    }
                    break;
                case 3:
                    System.out.print("Enter name to remove: ");
                    String removeName = sc.nextLine();
                    if (pb.searchContact(removeName)) {
                        System.out.println("Contact removed!\n");
                        pb.removeContact(removeName);
                    } else {
                        System.out.println("Contact not found!\n");
                    }
                    break;
                case 4:
                    if (pb.displayAllContacts().isEmpty()) {
                        System.out.println("No contact saved...\n");
                    } else {
                        System.out.println("Name,PhoneNumber,Email");
                        for (Contact ct : pb.displayAllContacts()) {
                            System.out.println(ct.getName() + "," + ct.getPhoneNumber() + "," + ct.getEmail());
                        }
                        System.out.println(); // print an extra line
                    }
                    break;
                case 5:
                    pb.saveToCSV("contacts.csv");
                    System.out.println("Contacts saved!\n");
                    break;
                case 0:
                    System.out.println("Program ends....");
                    pb.saveToCSV("contacts.csv");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Choose from 1 to 5.\n");
                    break;
            }
            sc.close(); // close the scanner
        }

    }
}
