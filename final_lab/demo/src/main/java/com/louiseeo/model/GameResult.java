package com.louiseeo.model;

public class GameResult {
    private String imposterUsername;
    private String realWord;
    private String hintWord;
    private String winner;

    public GameResult() {}

    public GameResult(String imposterUsername, String realWord, String hintWord, String winner) {
        this.imposterUsername = imposterUsername;
        this.realWord = realWord;
        this.hintWord = hintWord;
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

    public String gethintWord() {
        return hintWord;
    }

    public void sethintWord(String hintWord) {
        this.hintWord = hintWord;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
