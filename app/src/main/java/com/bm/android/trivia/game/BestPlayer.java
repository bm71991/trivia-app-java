package com.bm.android.trivia.game;

import com.google.firebase.firestore.Exclude;


public class BestPlayer    {
    public int easyCount;
    public int mediumCount;
    public int hardCount;
    @Exclude
    public String email;

    public void setEmail(String email)   {
        this.email = email;
    }
}

