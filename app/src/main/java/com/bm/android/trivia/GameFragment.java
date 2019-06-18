package com.bm.android.trivia;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class GameFragment extends Fragment {
    private ProgressBar mProgressBar;
    private TextView mQuestionTextView;
    private GameFragmentCallback mCallback;
    private Button mToSummaryButton;
    private String TAG = "GameFragment";
    private LiveData<ArrayList<TriviaQuestion>> mQuestions;
    private GameViewModel mGameViewModel;

    /*Hosting activity must implement - see onAttach*/
    public interface GameFragmentCallback {
        void onFinishGame();
//        LiveData<ArrayList<TriviaQuestion>> getTriviaQuestions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mGameViewModel = ViewModelProviders.of(getActivity()).get(GameViewModel.class);
        mGameViewModel.loadQuestions();
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
        mToSummaryButton = view.findViewById(R.id.to_summary_button);
        mQuestionTextView = view.findViewById(R.id.questionText);
        mQuestions = mGameViewModel.getQuestions();

        /*If the questions have not been loaded in the ViewModel yet: */
        if (mQuestions.getValue() == null)  {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            /*Observe when the questions have been loaded by the ViewModel*/
            mQuestions.observe(this, triviaQuestions -> {
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    mToSummaryButton.setVisibility(Button.VISIBLE);
                    displayQuestion(triviaQuestions, 0);
            });
            /*If the questions have already been loaded in the ViewModel:*/
        } else {
            displayQuestion(mQuestions.getValue(), 0);
        }
        mToSummaryButton.setOnClickListener(v -> mCallback.onFinishGame());
        return view;
    }

    private void displayQuestion(ArrayList<TriviaQuestion> triviaQuestions, int questionNumber)    {
        TriviaQuestion questionToDisplay = triviaQuestions.get(questionNumber);
        String questionString = questionToDisplay.getQuestion();
        if (mQuestionTextView.getVisibility() == View.INVISIBLE)    {
            mQuestionTextView.setVisibility(View.VISIBLE);
        }
        mQuestionTextView.setText(Html.fromHtml(questionString));
    }

}
