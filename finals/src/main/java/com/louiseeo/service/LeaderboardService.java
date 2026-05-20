package com.louiseeo.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.louiseeo.model.LeaderboardPlayer;

/**
 * Manages leaderboard data and player points.
 * Loads, saves, updates, and displays rankings.
 *
 * @author louiseeo
 */
public class LeaderboardService {

    private static final String FILE = "data/leaderboard.json";

    public static List<LeaderboardPlayer> loadLeaderboard() {
        try (FileReader reader = new FileReader(FILE)) {
            Type type = new TypeToken<List<LeaderboardPlayer>>() {}.getType();
            List<LeaderboardPlayer> players = new Gson().fromJson(reader, type);
            if (players != null) {
                return players;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void saveLeaderboard(List<LeaderboardPlayer> players) {
        try (FileWriter writer = new FileWriter(FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(players, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePoints(String username, int points) {
        List<LeaderboardPlayer> players = loadLeaderboard();
        LeaderboardPlayer found = null;

        for (LeaderboardPlayer player : players) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                found = player;
                break;
            }
        }

        if (found == null) {
            found = new LeaderboardPlayer(username, 0);
            players.add(found);
        }

        found.setPoints(found.getPoints() + points);
        saveLeaderboard(players);
    }

    public static String displayLeaderboard() {
        List<LeaderboardPlayer> players = loadLeaderboard();
        players.sort(Comparator.comparingInt(LeaderboardPlayer::getPoints).reversed());

        StringBuilder sb = new StringBuilder();
        sb.append(UIService.leaderboardHeader());

        int rank = 1;
        for (LeaderboardPlayer player : players) {
            sb.append(UIService.leaderboardRow(rank++, player.getUsername(), player.getPoints()))
              .append("\n");
        }

        sb.append(UIService.leaderboardFooter());
        return sb.toString();
    }
}