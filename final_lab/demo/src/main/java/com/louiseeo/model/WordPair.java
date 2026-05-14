package com.louiseeo.model;

public class WordPair {
    private String real;
    private String decoy;

    public WordPair() {}
    
    public WordPair(String real, String decoy) {
        this.real = real;
        this.decoy = decoy;
    }

    public void setReal(String real) {
        this.real = real;
    }

    public void setDecoy(String decoy) {
        this.decoy = decoy;
    }

    public String getReal() {
        return real;
    }

    public String getDecoy() {
        return decoy;
    }

}
