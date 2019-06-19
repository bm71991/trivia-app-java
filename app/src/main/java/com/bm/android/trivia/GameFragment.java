package com.bm.android.trivia;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
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
    private LiveData<ArrayList<TriviaQuestion>> mQuestionsLiveData;
    private GameViewModel mGameViewModel;
    private SummaryViewModel mSummaryViewModel;
    private LiveData<Integer> mCurrentQuestionIndex;

    /*Hosting activity must implement - see onAttach*/
    public interface GameFragmentCallback {
        void onFinishGame();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mGameViewModel = ViewModelProviders.of(getActivity()).get(GameViewModel.class);
        mSummaryViewModel = ViewModelProviders.of(getActivity())
                .get(SummaryViewModel.class);
        mGameViewModel.loadQuestions();
        mCurrentQuestionIndex = mGameViewModel.getCurrentQuestionIndex();
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

        mQuestionsLiveData = mGameViewModel.getQuestionsLiveData();

        /*If the questions have not been loaded in the ViewModel yet: */
        if (mQuestionsLiveData.getValue() == null)  {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            /*Observe when the questions have been loaded by the ViewModel*/
            mQuestionsLiveData.observe(this, triviaQuestions -> {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    playGame();
            });
            /*If the questions have already been loaded in the ViewModel:*/
        } else {
            playGame();
        }
        return view;
    }

    private void displayQuestionAndAnswers()    {
        TriviaQuestion questionToDisplay = getCurrentQuestion();
        String questionString = questionToDisplay.getQuestion();
        mQuestionTextView.setText(Html.fromHtml(questionString));
        displayAnswers(questionToDisplay.getAnswers());
    }

    private void displayAnswers(ArrayList<String> answers)   {
        for (String answer : answers)  {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(View.generateViewId());
            radioButton.setText(Html.fromHtml(answer));
            mRadioGroup.addView(radioButton);
        }
    }

    private void displayQuestionViews() {
            mQuestionTextView.setVisibility(View.VISIBLE);
            mRadioGroup.setVisibility(View.VISIBLE);
            mSubmitAnswerButton.setVisibility(View.VISIBLE);
    }

    private TriviaQuestion getCurrentQuestion()   {
        ArrayList<TriviaQuestion> questionList = mQuestionsLiveData.getValue();
        int currentQuestionIndex = mCurrentQuestionIndex.getValue();
        return questionList.get(currentQuestionIndex);
    }

    private void playGame() {
        displayQuestionViews();
        displayQuestionAndAnswers();

        mSubmitAnswerButton.setOnClickListener(v -> {
            int selectedButtonId = mRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedButton = GameFragment.this.getView().
                    findViewById(selectedButtonId);
            String correctAnswer = getCurrentQuestion().getCorrectAnswer();
            String selectedButtonText = selectedButton.getText().toString();
            if (selectedButtonText.equals(correctAnswer)) {
                mGameViewModel.incrementCorrectAnswerCount();
            }
            mRadioGroup.removeAllViews();
            mGameViewModel.incrementCurrentQuestionIndex();

            if (mCurrentQuestionIndex.getValue() <= mQuestionsLiveData.getValue().size() - 1) {
                displayQuestionAndAnswers();
            } else {
                mSummaryViewModel.setFinalScore(mQuestionsLiveData.getValue().size(),
                    mGameViewModel.getCorrectAnswerCount());
                mGameViewModel.resetGame();
                mCallback.onFinishGame();
            }
        });
    }
}