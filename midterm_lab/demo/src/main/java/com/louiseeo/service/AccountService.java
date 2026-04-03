package com.louiseeo.service;

import com.louiseeo.model.Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class AccountService {
    private static final String FILE_PATH = "data/accounts.json";
    private Gson gson;
    private List<Account> accounts;

    public AccountService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.accounts = new ArrayList<>();
        loadAccounts();
    }

    private void loadAccounts() {
        List<Account> accounts = new ArrayList<>();

        File file = new File(FILE_PATH);
        if (!file.exists())
            return;

        try (FileReader fr = new FileReader(FILE_PATH)) {

            Type accountsType = new TypeToken<List<Account>>() {
            }.getType();

            List<Account> acc = gson.fromJson(fr, accountsType);

            if (acc != null) {
                accounts = acc;
            }

        } catch (IOException e) {
            System.out.println("Error loading file: " + FILE_PATH + " -> " + e.getMessage());
        }
        this.accounts = accounts;
    }

    private void saveAccounts() {
        try (FileWriter fw = new FileWriter(FILE_PATH)) {
            gson.toJson(accounts, fw);
            System.out.println("\nPlayer data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + FILE_PATH + " -> " + e.getMessage());
            System.out.println("Player data not saved. Try again.");
        }
    }

    public boolean usernameExist(String username) {
        for (Account acc : accounts) {
            if (username.equals(acc.getUsername()))
                return true;
        }
        return false;
    }

    public Account register(String username, String password) {
        if (usernameExist(username)) {
            return null;
        }
        Account acc = new Account(username, password);
        accounts.add(acc);
        saveAccounts();
        return acc;
    }

    public Account login(String username, String password) {
        for (Account acc : accounts) {
            if (username.equals(acc.getUsername()) && password.equals(acc.getPassword()))
                return acc;
        }
        return null;
    }

    public void updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUsername().equals(account.getUsername())) {
                accounts.set(i, account);
                saveAccounts();
                return;
            }
        }
    }
}
