package com.bm.android.trivia.game;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import android.util.Log;

/*used by SummaryFragment and for communication between GameFragment and SummaryFragment*/
public class SummaryViewModel extends AndroidViewModel {
    private double finalScore;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        finalScore = -1;
    }

    public void setFinalScore(double numberOfQuestions, double correctAnswerCount)    {
        finalScore = correctAnswerCount / numberOfQuestions;
    }

    public String getFinalScore()   {
        int percentageNumber = (int) Math.round(finalScore * 100);
        return percentageNumber + "%";
    }
}