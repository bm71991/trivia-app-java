package com.bm.android.trivia.api_call;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Random;

public class TriviaQuestion {
    private String question;
    @SerializedName("correct_answer")
    private String correctAnswer;
    @SerializedName("incorrect_answers")
    private ArrayList<String> incorrectAnswers;
    private int correctAnswerIndex;
    private ArrayList<String> allAnswers;

    public String getCorrectAnswer()   {
        return correctAnswer;
    }

    public String getQuestion()    {
        return question;
    }

    public ArrayList<String> getAnswers()   {
        if (allAnswers == null)    {
            int answerCount = incorrectAnswers.size() + 1;
            Random random = new Random();
            /* Choose a random position for the correct answer to be at in the list */
            correctAnswerIndex = random.nextInt(answerCount);
            ArrayList<String> answersToDisplay = new ArrayList<>();
            answersToDisplay.addAll(incorrectAnswers);
            answersToDisplay.add(correctAnswerIndex, correctAnswer);
            allAnswers = answersToDisplay;
        }
        return allAnswers;
    }
}
