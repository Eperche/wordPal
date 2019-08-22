package com.example.englishdic;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import static java.text.DateFormat.getDateTimeInstance;

public class Dictionary {


    private String word,meaing,sentence,meaningSentence;
    private Map<String, String> timestamp;
    public Dictionary() {

    }

    public void DictionarySet(String sentence,String meaningSentence) {
        this.sentence = sentence;
        this.meaningSentence = meaningSentence;
    }

    public Dictionary(String word, String meaing) {
        this.word = word;
        this.meaing = meaing;
    }
    public void setTimestamp(Map<String, String> timeStamp) {
        this.timestamp= timestamp;
    }
    public Map<String, String> getTimestamp() {
        return timestamp;
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

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
    public String getMeaningSentence() {
        return meaningSentence;
    }

    public void setMeaningSentence(String meaningSentence) {
        this.meaningSentence = meaningSentence;
    }
    public void setMeaing(String meaing) {
        this.meaing = meaing;
    }

    public static String getTimeDate(long timestamp)
    {
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
}
