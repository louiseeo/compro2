package com.phonebook.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.phonebook.models.Contact;

public class PhonebookService {
    private Map<String, Contact> contacts = new HashMap<>();

    // Method to add contact to hashmap
    public void addContact(Contact c) {
        contacts.put(c.getName(), c);
    }

    // Method to search a contact
    public boolean searchContact(String name) {
        return contacts.containsKey(name);
    }
    
    // Method that removes a contact
    public void removeContact(String name) {
        contacts.remove(name);
    }

    // Save all the contacts to CSV
    public void saveToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("Name,PhoneNumber,Email\n");
            for (Contact c : contacts.values()) {
                bw.write(c.toCsvString()); // writes contact to CSV file
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for reading CSV file
    public void loadFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // stops the header from printing again
                    continue;
                }
                String[] cols = line.split(",");
                if (cols.length == 3) {
                    String name = cols[0];
                    String phoneNumber = cols[1];
                    String email = cols[2];
                    Contact c = new Contact(name, phoneNumber, email);
                    contacts.put(name, c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for displaying contacts
    public Collection<Contact> displayAllContacts() {
        return contacts.values();
    }
}
