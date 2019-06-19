package com.bm.android.trivia;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
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

    public double getFinalScore()   {
        return finalScore;
    }
}
