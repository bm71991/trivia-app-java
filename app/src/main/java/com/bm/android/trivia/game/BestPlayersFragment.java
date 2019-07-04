package com.bm.android.trivia.game;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bm.android.trivia.R;
import com.bm.android.trivia.game.viewmodels.BestPlayersViewModel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

public class BestPlayersFragment extends Fragment {
    private BestPlayersViewModel mViewModel;
    private static String PICKER_DIALOG_TAG = "pickerDialogTag";
    private static String CATEGORY_TAG = "category";
    private static String DIFFICULTY_TAG = "difficulty";
    private Button mCategoryButton;
    private Button mDifficultyButton;
    private Button mSubmitButton;
    private BestPlayersFragmentCallback mCallback;

    public static BestPlayersFragment newInstance() {
        return new BestPlayersFragment();
    }


    public interface BestPlayersFragmentCallback    {
        void onStartPicker();
        void onStartBestPlayersDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(BestPlayersViewModel.class);
    }

    @Override
    public void onAttach(Context context)  {
        super.onAttach(context);
        mCallback = (BestPlayersFragmentCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.best_players_fragment, container, false);
        mCategoryButton = view.findViewById(R.id.best_category_btn);
        mDifficultyButton = view.findViewById(R.id.best_difficulty_btn);
        mSubmitButton = view.findViewById(R.id.get_best_button);


        /* Set listeners to start SetupPickerFragment if button is selected */
        mCategoryButton.setOnClickListener(v -> startPickerDialog(CATEGORY_TAG));
        mDifficultyButton.setOnClickListener(v -> startPickerDialog(DIFFICULTY_TAG));

        LiveData<String> categoryChosenLiveData = mViewModel.getCategoryChosen();
        LiveData<String> difficultyChosenLiveData = mViewModel.getDifficultyChosen();

        /* observe when SetupPickerFragment changes these fields in SetupViewModel:
         * update UI accordingly. */
        categoryChosenLiveData.observe(this, chosenCategory -> {
            mCategoryButton.setText(chosenCategory);
        });

        difficultyChosenLiveData.observe(this, chosenDifficulty ->
                mDifficultyButton.setText(chosenDifficulty));

        mSubmitButton.setOnClickListener(v -> {
            mCallback.onStartBestPlayersDialog();
        });

        return view;
    }

    private void startPickerDialog(String dialogType)    {
        mViewModel.setDialogType(dialogType);
        mCallback.onStartPicker();
    }

    @Override
    public void onDetach()  {
        super.onDetach();
    }
}
