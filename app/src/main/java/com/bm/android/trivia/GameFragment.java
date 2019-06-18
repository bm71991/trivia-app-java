package com.bm.android.trivia;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class GameFragment extends Fragment {
    private ProgressBar mProgressBar;
    private TextView mQuestionTextView;
    private GameFragmentCallback mCallback;
    private Button mSubmitAnswerButton;
    private RadioGroup mRadioGroup;
    private String TAG = "GameFragment";
    private LiveData<ArrayList<TriviaQuestion>> mQuestions;
    private GameViewModel mGameViewModel;

    /*Hosting activity must implement - see onAttach*/
    public interface GameFragmentCallback {
        void onFinishGame();
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
        mSubmitAnswerButton = view.findViewById(R.id.submit_answer_button);
        mQuestionTextView = view.findViewById(R.id.questionText);
        mRadioGroup = view.findViewById(R.id.questions_radio_group);

        mQuestions = mGameViewModel.getQuestions();

        /*If the questions have not been loaded in the ViewModel yet: */
        if (mQuestions.getValue() == null)  {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            /*Observe when the questions have been loaded by the ViewModel*/
            mQuestions.observe(this, triviaQuestions -> {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    playGame(triviaQuestions);
            });
            /*If the questions have already been loaded in the ViewModel:*/
        } else {
            playGame(mQuestions.getValue());
        }
        return view;
    }

    private void displayQuestionAndAnswers(TriviaQuestion questionToDisplay)    {
        String questionString = questionToDisplay.getQuestion();
        if (mQuestionTextView.getVisibility() == View.GONE)    {
            mQuestionTextView.setVisibility(View.VISIBLE);
        }
        if (mRadioGroup.getVisibility() == View.GONE)    {
            mRadioGroup.setVisibility(View.VISIBLE);
        }

        mQuestionTextView.setText(Html.fromHtml(questionString));
        displayAnswers(questionToDisplay.getAnswers());
    }

    private void displayAnswers(ArrayList<String> answers)   {
        for (String answer : answers)  {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(View.generateViewId());
            radioButton.setText(answer);
            mRadioGroup.addView(radioButton);
        }
    }

    private void playGame(ArrayList<TriviaQuestion> questions) {
        final int lastQuestionIndex = questions.size() - 1;
        TriviaQuestion currentQuestion = questions.get(
                mGameViewModel.getCurrentQuestionIndex());
        displayQuestionAndAnswers(currentQuestion);
        if (mSubmitAnswerButton.getVisibility() == View.GONE)   {
            mSubmitAnswerButton.setVisibility(View.VISIBLE);
        }

        mSubmitAnswerButton.setOnClickListener(v -> {
           mRadioGroup.removeAllViews();
           mGameViewModel.incrementCurrentQuestionIndex();
           int currentGameIndex = mGameViewModel.getCurrentQuestionIndex();

           if (currentGameIndex <= lastQuestionIndex)   {
               TriviaQuestion nextQuestion = questions.get(currentGameIndex);
               displayQuestionAndAnswers(nextQuestion);
           } else {
               mGameViewModel.resetGame();
               mCallback.onFinishGame();
           }
        });
    }
}