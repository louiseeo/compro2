package com.louiseeo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.louiseeo.model.Player;

public class FileService {

        /**
     * Loads the word bank from the specified file.
     *
     * @param filename : the path to the file containing words
     * @param out    : the output stream to send error messages to the client
     * @return a list of words loaded from the file
     */
    public static List<String> loadWordbank(String filename, PrintWriter out) {
        List<String> words = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Error: File not found -> " + filename);
            return words; // return empty list safely
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + filename + " -> " + e.getMessage());
        }
        return words;
    }



    /**
     * Loads the list of players from a JSON file.
     *
     * @param filename : the path to the JSON file containing player data
     * @param out      : the output stream to send error messages to the client
     * @return a list of players loaded from the file
     */
    public static List<Player> loadPlayers(String filename, PrintWriter out) {
        List<Player> playerList = new ArrayList<>();

        try (FileReader fr = new FileReader(filename)) {
            Gson gson = new Gson();

            Type playerListType = new TypeToken<List<Player>>() {
            }.getType();

            List<Player> pl = gson.fromJson(fr, playerListType);

            if (pl != null) {
                playerList = pl;
            }

        } catch (IOException e) {
            System.out.println("Error loading file: " + filename + " -> " + e.getMessage());
        }
        return playerList;
    }

    /**
     * Saves the list of players to JSON file.
     * 
     * @param filename : the path to the JSON file where player data will be saved
     * @param players  : the list of players to be saved
     * @param out      : the output stream to send confirmation or error messages to the client
     */
    public static void savePlayers(String filename, List<Player> players, PrintWriter out) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(players, fw);
            out.println("\nPlayer data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + filename + " -> " + e.getMessage());
            out.println("Player data not saved. Try again.");
        }
    }
    
}