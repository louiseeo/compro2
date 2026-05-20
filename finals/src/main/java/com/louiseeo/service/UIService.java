package com.louiseeo.service;

/**
 * Handles all UI display strings for the UnderCoven game.
 * Clean, minimal console UI with consistent formatting.
 *
 * @author louiseeo
 */
public class UIService {

    // ─── Layout Constants ─────────────────────────────────────────────────────

    private static final String THIN  = "- - - - - - - - - - - - - - - - - - - -";
private static final String THICK = "========================================";
    private static final int    WIDTH = 42;

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static String center(String text) {
        int padding = (WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    private static String box(String title) {
        return THICK + "\n"
             + center(title) + "\n"
             + THICK;
    }

    // ─── Menus ────────────────────────────────────────────────────────────────

    public static String mainMenu() {
        return "\n"
             + box("U N D E R C O V E N") + "\n"
             + "\n"
             + "  [ 1 ]  Log In\n"
             + "  [ 2 ]  Sign Up\n"
             + "  [ 3 ]  Leaderboard\n"
             + "  [ 0 ]  Exit\n"
             + "\n"
             + THIN + "\n"
             + "Choice: ";
    }

    // ─── Game Phases ──────────────────────────────────────────────────────────

    public static String chatPhase() {
        return "\n"
             + box("CHAT PHASE") + "\n"
             + "\n"
             + "  Give clues about your secret word.\n"
             + "  Type  'vote'  when you're suspicious.\n"
             + "\n"
             + THIN + "\n";
    }

    public static String votingPhase() {
        return "\n"
             + box("VOTING PHASE") + "\n"
             + "\n"
             + "  Who do you think is the imposter?\n"
             + "\n"
             + THIN + "\n";
    }

    public static String playAgain() {
        return "\n"
             + box("PLAY AGAIN?") + "\n"
             + "\n"
             + "  Would you like another round?\n"
             + "\n"
             + "  Type  'yes'  or  'no'\n"
             + "\n"
             + THIN + "\n"
             + "Response: ";
    }

    // ─── Role Reveals ─────────────────────────────────────────────────────────

    public static String citizenRole(String word) {
        return "\n"
             + box("ROLE ASSIGNED") + "\n"
             + "\n"
             + "  Role         :  CITIZEN\n"
             + "  Secret Word  :  " + word + "\n"
             + "\n"
             + "  Find the imposter before it's too late.\n"
             + "\n"
             + THIN + "\n";
    }

    public static String imposterRole(String hint) {
        return "\n"
             + box("ROLE ASSIGNED") + "\n"
             + "\n"
             + "  Role         :  IMPOSTER\n"
             + "  Hint         :  " + hint + "\n"
             + "\n"
             + "  Blend in. Don't get caught.\n"
             + "\n"
             + THIN + "\n";
    }

    // ─── Game Results ─────────────────────────────────────────────────────────

    public static String gameResults(
            String eliminated,
            String imposter,
            String word,
            String winner) {
        return "\n"
             + box("GAME RESULTS") + "\n"
             + "\n"
             + "  Eliminated   :  " + eliminated + "\n"
             + "  Imposter     :  " + imposter   + "\n"
             + "  Secret Word  :  " + word       + "\n"
             + "\n"
             + THICK + "\n"
             + center(winner) + "\n"
             + THICK + "\n";
    }

    // ─── Leaderboard ──────────────────────────────────────────────────────────

    public static String leaderboardHeader() {
        return "\n"
             + box("LEADERBOARD") + "\n"
             + "\n"
             + String.format("  %-4s  %-18s  %s\n", "#", "Username", "Points")
             + "  " + THIN + "\n";
    }

    public static String leaderboardRow(int rank, String username, int points) {
        return String.format("  %-4d  %-18s  %d", rank, username, points);
    }

    public static String leaderboardFooter() {
        return "  " + THIN + "\n";
    }

    // ─── Status Tags ──────────────────────────────────────────────────────────

    public static String success(String text) {
        return "  /  " + text;
    }

    public static String error(String text) {
        return "  X  " + text;
    }

    public static String system(String text) {
        return "  »  " + text;
    }

    public static String tip(String text) {
        return "  ·  " + text;
    }

    // ─── Dividers ─────────────────────────────────────────────────────────────

    public static String divider() {
        return THIN;
    }

    public static String thickDivider() {
        return THICK;
    }
}