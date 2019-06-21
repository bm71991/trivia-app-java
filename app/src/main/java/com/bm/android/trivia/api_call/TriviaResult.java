package com.bm.android.trivia.api_call;

import com.bm.android.trivia.api_call.TriviaQuestion;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TriviaResult {
    @SerializedName("results")
    ArrayList<TriviaQuestion> questions;

    public ArrayList<TriviaQuestion> getQuestions()   {
        return questions;
    }
}
