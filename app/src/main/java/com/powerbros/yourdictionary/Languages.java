package com.powerbros.yourdictionary;

import androidx.annotation.NonNull;

public class Languages {

    public String name;  // Display name getDisplayName
    private String  getName;  // Kısaltma getLanguage --> es,en

    public Languages(String name, String getName) {
        this.name = name;
        this.getName = getName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGetName() {
        return getName;
    }

    public void setGetName(String getName) {
        this.getName = getName;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
