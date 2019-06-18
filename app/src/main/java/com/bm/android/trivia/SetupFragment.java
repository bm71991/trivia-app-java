package com.bm.android.trivia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SetupFragment extends Fragment {
    private SetupFragmentCallback mCallback;
    private Button categoryButton;
    private Button difficultyButton;
    private Button startButton;
    private static String PICKER_DIALOG_TAG = "pickerDialogTag";
    private static String CATEGORY_TAG = "category";
    private static String DIFFICULTY_TAG = "difficulty";

    /*Hosting activity must implement - see onAttach*/
    public interface SetupFragmentCallback {
        void onStartGame();
    }

    public static SetupFragment newInstance()  {
        return new SetupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.setup_fragment, container, false);
        categoryButton = view.findViewById(R.id.category_button);
        difficultyButton = view.findViewById(R.id.difficulty_button);

        categoryButton.setOnClickListener(v -> startPickerDialog(CATEGORY_TAG));
        difficultyButton.setOnClickListener(v -> startPickerDialog(DIFFICULTY_TAG));

        startButton = view.findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> mCallback.onStartGame());
        return view;
    }

    @Override
    public void onResume()  {
        super.onResume();

    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (SetupFragmentCallback) context;
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }

    private void startPickerDialog(String pickerType)    {
        FragmentManager fm = getFragmentManager();
        PickerFragment pickerDialog = PickerFragment.newInstance(pickerType);
        pickerDialog.setTargetFragment(SetupFragment.this, );
        pickerDialog.show(fm, PICKER_DIALOG_TAG);
    }
}
