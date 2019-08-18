package com.example.englishdic;

public class Dictionary {

    private String word,meaing;

    public Dictionary() {

    }

    public Dictionary(String word, String meaing) {
        this.word = word;
        this.meaing = meaing;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaing() {
        return meaing;
    }

    public void setMeaing(String meaing) {
        this.meaing = meaing;
    }
}
