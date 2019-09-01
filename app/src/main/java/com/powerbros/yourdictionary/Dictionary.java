package com.powerbros.yourdictionary;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.text.DateFormat.getDateTimeInstance;

public class Dictionary {


    private String word,meaning,sentence,meaningSentence;

    private Map<String, String> timestamp;
    private Map<String, String> wordLang;
    public Dictionary() {

    }



    public void DictionarySet(String sentence,String meaningSentence) {
        this.sentence = sentence;
        this.meaningSentence = meaningSentence;
    }

    public Dictionary(String word, String meaing) {
        this.word = word;
        this.meaning = meaing;
    }

    public Map<String, String> getWordLang() {
        return wordLang;
    }

    public void setWordLang(Map<String, String> wordLang) {
        this.wordLang = wordLang;
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

    public String getMeaning() {
        return meaning;
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
   public void setMeaning(String meaing) {
        this.meaning = meaing;
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
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("word", word);
        result.put("meaning", meaning);

        return result;
    }
}
