package com.bm.android.trivia.game;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bm.android.trivia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SetupFragment extends Fragment {
    private SetupFragmentCallback mCallback;
    private Button categoryButton;
    private Button difficultyButton;
    private Button startButton;
    private static String PICKER_DIALOG_TAG = "pickerDialogTag";
    private static String CATEGORY_TAG = "category";
    private static String DIFFICULTY_TAG = "difficulty";
    private SetupViewModel mSetupViewModel;

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
        /* same instance of SetupViewModel used between SetupFragment and PickerFragment */
        mSetupViewModel = ViewModelProviders.of(getActivity()).get(SetupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {


//        delete
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null)   {
//            Log.i("test", "user = " +  user.getEmail() + " in SetupFragment");
//        } else  {
//            Log.i("test", "user = " +  user + " in SetupFragment");
//        }
//        delete


        View view = inflater.inflate(R.layout.setup_fragment, container, false);
        categoryButton = view.findViewById(R.id.category_button);
        difficultyButton = view.findViewById(R.id.difficulty_button);

        /* Set listeners to start PickerFragment if button is selected */
        categoryButton.setOnClickListener(v -> startPickerDialog(CATEGORY_TAG));
        difficultyButton.setOnClickListener(v -> startPickerDialog(DIFFICULTY_TAG));
        startButton = view.findViewById(R.id.start_button);

        LiveData<String> categoryChosen = mSetupViewModel.getCategoryChosen();
        LiveData<String> difficultyChosen = mSetupViewModel.getDifficultyChosen();

        /* observe when PickerFragment changes these fields in SetupViewModel:
         * update UI accordingly. */
        categoryChosen.observe(this, chosenCategory -> {
                    categoryButton.setText(chosenCategory);
                });

        difficultyChosen.observe(this, chosenDifficulty ->
                difficultyButton.setText(chosenDifficulty));

        /* need to add validation here */
        startButton.setOnClickListener(v -> {
                    GameViewModel gameViewModel = ViewModelProviders.of(getActivity())
                            .get(GameViewModel.class);
                    gameViewModel.setCategory((String) categoryButton.getText());
                    gameViewModel.setDifficulty((String) difficultyButton.getText());
                    mCallback.onStartGame();
                });
        return view;
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
        mSetupViewModel.clearDialogType();
        mSetupViewModel.resetChosenOptions();
    }

    private void startPickerDialog(String dialogType)    {
        FragmentManager fm = getFragmentManager();
        PickerFragment pickerDialog = PickerFragment.newInstance();
        mSetupViewModel.setDialogType(dialogType);
        pickerDialog.show(fm, PICKER_DIALOG_TAG);
    }
}
