package main.java.com.louiseeo.model;

public class Player {
    private String name;
    private int highScore;

    public Player(String name, int highScore) {
        this.name = name;
        this.highScore = highScore;
    }
    public Player(){}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getHighScore() {
        return highScore;
    }
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    


}
