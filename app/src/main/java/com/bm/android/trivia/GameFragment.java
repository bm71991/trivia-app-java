package com.bm.android.trivia;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class GameFragment extends Fragment {
    private ProgressBar mProgressBar;
    private TextView mQuestionTextView;
    private GameFragmentCallback mCallback;

    /*Hosting activity must implement - see onAttach*/
    public interface GameFragmentCallback {
        void onFinishGame();
        LiveData<ArrayList<QuizQuestion>> getQuizQuestions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (GameFragmentCallback) context;
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }

    public static GameFragment newInstance()   {
        return new GameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.game_fragment, container, false);
        mProgressBar = view.findViewById(R.id.progressBar);
        mQuestionTextView = view.findViewById(R.id.questionText);
        LiveData<ArrayList<QuizQuestion>> questions = mCallback.getQuizQuestions();

        /*If the questions have not been loaded in the ViewModel yet: */
        if (questions.getValue() == null)  {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            /*Observe when the questions have been loaded by the ViewModel*/
            questions.observe(this, new Observer<ArrayList<QuizQuestion>>() {
                @Override
                public void onChanged(@Nullable ArrayList<QuizQuestion> quizQuestions) {
                    displayQuestion(quizQuestions, 0);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            });
            /*If the questions have already been loaded in the ViewModel:*/
        } else {
            displayQuestion(questions.getValue(), 0);
        }
        return view;
    }

    private void displayQuestion(ArrayList<QuizQuestion> quizQuestions, int questionNumber)    {
        QuizQuestion questionToDisplay = quizQuestions.get(questionNumber);
        String questionString = questionToDisplay.getQuestion();
        if (mQuestionTextView.getVisibility() == View.INVISIBLE)    {
            mQuestionTextView.setVisibility(View.VISIBLE);
        }
        mQuestionTextView.setText(Html.fromHtml(questionString));
    }
}
