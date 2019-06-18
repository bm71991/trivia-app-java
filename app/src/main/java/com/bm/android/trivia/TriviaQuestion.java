package com.bm.android.trivia;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class TriviaQuestion {
    private String difficulty;
    private String question;
    @SerializedName("correct_answer")
    private String correctAnswer;
    @SerializedName("incorrect_answers")
    private ArrayList<String> incorrectAnswers;
    private int correctAnswerIndex;

    String getCorrectAnswer()   {
        return correctAnswer;
    }

    String getQuestion()    {
        return question;
    }

    public ArrayList<String> getAnswers()   {
        int answerCount = incorrectAnswers.size() + 1;
        Random random = new Random();
        /* Choose a random position for the correct answer to be at in the list */
        correctAnswerIndex = random.nextInt(answerCount);
        ArrayList<String> answersToDisplay = new ArrayList<String>();
        answersToDisplay.addAll(incorrectAnswers);
        answersToDisplay.add(correctAnswerIndex, correctAnswer);
        return answersToDisplay;
    }
}
