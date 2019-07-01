package com.bm.android.trivia.user_access;

import com.google.firebase.firestore.Exclude;

public class User {
    private String email;
    private int easy;
    @Exclude
    private String id;

    public String getId()   {
        return id;
    }
    public void setId(String id)   {
        this.id = id;
    }
    public int getEasy()    {
        return easy;
    }

}
