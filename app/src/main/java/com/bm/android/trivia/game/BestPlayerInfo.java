package com.bm.android.trivia.game;

import com.google.firebase.firestore.Exclude;

public class BestPlayerInfo {
    private String email;
    private int easyCount;
    private int mediumCount;
    private int hardCount;
    private String id;

    public String getId()   {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public int getEasyCount() {
        return easyCount;
    }

    public int getMediumCount() {
        return mediumCount;
    }

    public int getHardCount() {
        return hardCount;
    }
}
