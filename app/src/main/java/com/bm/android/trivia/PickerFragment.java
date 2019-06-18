package com.bm.android.trivia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class PickerFragment extends DialogFragment {
    private final static String OPTION_TYPE = "optionType";
    private final static String CATEGORY_TAG = "category";
    private final static String DIFFICULTY_TAG = "difficulty";
    private String[] options;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String pickerType = getArguments().getString(OPTION_TYPE);
        String title = getTitle(pickerType);
        options = getOptions(pickerType);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.options_picker, null);
        NumberPicker optionsPicker = v.findViewById(R.id.options_picker);
        configureOptionsPicker(optionsPicker);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    public static PickerFragment newInstance(String pickerType) {
        PickerFragment fragment = new PickerFragment();
        Bundle args = new Bundle();
        if (pickerType.equals(CATEGORY_TAG))  {
            args.putString(OPTION_TYPE, CATEGORY_TAG);
        } else {
            args.putString(OPTION_TYPE, DIFFICULTY_TAG);
        }
        fragment.setArguments(args);
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
        Resources r = getResources();
        if (pickerType.equals(CATEGORY_TAG))    {
            return r.getStringArray(R.array.category_options);
        } else {
            return r.getStringArray(R.array.difficulty_options);
        }
    }
}
