package com.louiseeo.model;

public class GameResult {
    private String imposterUsername;
    private String realWord;
    private String decoyWord;
    private String winner;

    public GameResult() {}

    public GameResult(String imposterUsername, String realWord, String decoyWord, String winner) {
        this.imposterUsername = imposterUsername;
        this.realWord = realWord;
        this.decoyWord = decoyWord;
        this.winner = winner;
    }

    public String getImposterUsername() {
        return imposterUsername;
    }

    public void setImposterUsername(String imposterUsername) {
        this.imposterUsername = imposterUsername;
    }

    public String getRealWord() {
        return realWord;
    }

    public void setRealWord(String realWord) {
        this.realWord = realWord;
    }

    public String getDecoyWord() {
        return decoyWord;
    }

    public void setDecoyWord(String decoyWord) {
        this.decoyWord = decoyWord;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
