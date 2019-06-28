package com.bm.android.trivia.game;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.bm.android.trivia.R;
import com.bm.android.trivia.game.viewmodels.BestPlayersViewModel;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class BestPlayersPickerFragment extends DialogFragment {
    private final static String CATEGORY_TAG = "category";
    private String[] options;
    private BestPlayersViewModel mBestPlayersViewModel;
    private String pickerType;
    private NumberPicker optionsPicker;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mBestPlayersViewModel = ViewModelProviders.of(getActivity()).get(BestPlayersViewModel.class);
        Log.i("test", "view model in dialog = " + mBestPlayersViewModel);
        pickerType = mBestPlayersViewModel.getDialogType();
        String title = getTitle(pickerType);
        options = getOptions(pickerType);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.options_picker, null);
        optionsPicker = v.findViewById(R.id.options_picker);
        configureOptionsPicker(optionsPicker);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    /* Index of the chosen value in the list of options for optionsPicker */
                    int optionsIndex = optionsPicker.getValue();
                    if (pickerType.equals(CATEGORY_TAG))    {
                        String optionChosen = mBestPlayersViewModel.getCategoryString(optionsIndex);
                        mBestPlayersViewModel.setCategory(optionChosen);
                    } else {
                        String optionChosen = mBestPlayersViewModel.getDifficultyString(optionsIndex);
                        mBestPlayersViewModel.setDifficulty(optionChosen);
                    }
                })
                .create();
    }

    public static BestPlayersPickerFragment newInstance() {
        BestPlayersPickerFragment fragment = new BestPlayersPickerFragment();
        return fragment;
    }

    private void configureOptionsPicker(NumberPicker optionsPicker)    {
        optionsPicker.setMaxValue(options.length - 1);
        optionsPicker.setMinValue(0);
        optionsPicker.setDisplayedValues(options);
        optionsPicker.setWrapSelectorWheel(false);
    }

    private String getTitle(String pickerType)  {
        if (pickerType.equals(CATEGORY_TAG))    {
            return getString(R.string.select_category);
        } else  {
            return getString(R.string.select_difficulty);
        }
    }

    private String[] getOptions(String pickerType)  {
        if (pickerType.equals(CATEGORY_TAG))    {
            return mBestPlayersViewModel.getCategoryOptions();
        } else {
            return mBestPlayersViewModel.getDifficultyOptions();
        }
    }
}
