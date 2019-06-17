package com.bm.android.trivia;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TriviaResult {
    @SerializedName("results")
    ArrayList<TriviaQuestion> questions;

    ArrayList<TriviaQuestion> getQuestions()   {
        return questions;
    }
}
