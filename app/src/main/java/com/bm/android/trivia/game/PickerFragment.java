package com.bm.android.trivia.game;

import android.app.AlertDialog;
import android.app.Dialog;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.bm.android.trivia.R;

public class PickerFragment extends DialogFragment {
    private final static String CATEGORY_TAG = "category";
    private String[] options;
    private SetupViewModel setupViewModel;
    private String pickerType;
    private NumberPicker optionsPicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setupViewModel = ViewModelProviders.of(getActivity()).get(SetupViewModel.class);
        pickerType = setupViewModel.getDialogType();
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
                        String optionChosen = setupViewModel.getCategoryString(optionsIndex);
                        setupViewModel.setCategory(optionChosen);
                    } else {
                        String optionChosen = setupViewModel.getDifficultyString(optionsIndex);
                        setupViewModel.setDifficulty(optionChosen);
                    }
                })
                .create();
    }

    public static PickerFragment newInstance() {
        PickerFragment fragment = new PickerFragment();
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
            return setupViewModel.getCategoryOptions();
        } else {
            return setupViewModel.getDifficultyOptions();
        }
    }
}
