package com.louiseeo.model;

public class WordPair {
    private String real;
    private String hint;

    public WordPair() {}
    
    public WordPair(String real, String hint) {
        this.real = real;
        this.hint = hint;
    }

    public void setReal(String real) {
        this.real = real;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getReal() {
        return real;
    }

    public String getHint() {
        return hint;
    }

}
