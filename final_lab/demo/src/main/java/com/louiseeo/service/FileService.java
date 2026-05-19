package com.louiseeo.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.louiseeo.model.WordPair;

/**
 * Handles file loading operations for the game.
 * Used for reading word bank data from JSON files.
 *
 * @author louiseeo
 */
public class FileService {

    /**
     * Loads the word bank from the specified file.
     *
     * @param filename : the path to the file containing pairs
     * @return a list of pairs loaded from the file
     */
    public static List<WordPair> loadWordbank(String filename) {
        List<WordPair> pairs = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Error: File not found -> " + filename);
            return pairs; // return empty list safely
        }

        try (FileReader fr = new FileReader(file)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<WordPair>>() {
            }.getType();
            List<WordPair> loaded = gson.fromJson(fr, type);
            if (loaded != null) {
                pairs = loaded;
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + filename + " -> " + e.getMessage());
        }
        return pairs;
    }

}