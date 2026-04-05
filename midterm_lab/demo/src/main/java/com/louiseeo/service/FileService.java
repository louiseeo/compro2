package com.louiseeo.service;

import com.louiseeo.model.Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

/**
 * Handles reading and writing of account data
 * to and from a JSON file using Gson.
 *
 * @author louiseeo
 */
public class FileService {

    /**
     * Loads the list of accounts from a JSON file.
     * Returns an empty list if the file does not exist.
     *
     * @param filename : path to the JSON file
     * @param out      : output stream for error messages
     * @return list of Account objects loaded from file
     */
    public static List<Account> loadAccounts(String filename, PrintWriter out) {
        List<Account> accounts = new ArrayList<>();

        try (FileReader fr = new FileReader(filename)) {
            Gson gson = new Gson();
            Type accountsType = new TypeToken<List<Account>>() {
            }.getType();

            List<Account> acc = gson.fromJson(fr, accountsType);

            if (acc != null) {
                accounts = acc;
            }

        } catch (IOException e) {
            System.out.println("Error loading file: " + filename + " -> " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Saves the list of accounts to a JSON file.
     * Overwrites existing file content.
     *
     * @param filename : path to the JSON file
     * @param accounts : list of Account objects to save
     * @param out      : output stream for error messages
     */
    public static void saveAccounts(String filename, List<Account> accounts, PrintWriter out) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(accounts, fw);
            System.out.println("\nPlayer data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + filename + " -> " + e.getMessage());
            System.out.println("Player data not saved. Try again.");
        }
    }


}
