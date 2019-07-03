package com.bm.android.trivia.user_access;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bm.android.trivia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SignupFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mUsernameEditText;
    private Button mSignupButton;
    private SignupFragmentCallback mCallback;
    private UserAccessViewModel mViewModel;
    private LinearLayout mSignupLayout;

    public interface SignupFragmentCallback {
        void onSignupSuccess();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (SignupFragmentCallback) context;
    }

    public static SignupFragment newInstance()  {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);
        mProgressBar = view.findViewById(R.id.auth_progress_bar);
        mEmailEditText = view.findViewById(R.id.email_input);
        mPasswordEditText = view.findViewById(R.id.password_input);
        mUsernameEditText = view.findViewById(R.id.username);
        mSignupButton = view.findViewById(R.id.signup_button);
        mSignupLayout = view.findViewById(R.id.sign_up_layout);

        mViewModel = ViewModelProviders.of(getActivity()).get(UserAccessViewModel.class);

        if (mViewModel.isQuerying())    {
            showProgressBar();
        }

        mSignupButton.setOnClickListener(v -> {
            mViewModel.setQueryFlag(true);
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            String username = mUsernameEditText.getText().toString();
            showProgressBar();
            mViewModel.signUp(email, password, username);
        });

        observeIsSuccessfulSigningUp();
        observeHadErrorSigningUp();

        return view;
    }

    public void observeIsSuccessfulSigningUp() {
        mViewModel.getIsSuccessfulSigningUp().observe(this, isSuccessful -> {
            mViewModel.setQueryFlag(false);
            if (isSuccessful)   {
                mCallback.onSignupSuccess();
            }
        });
    }

    public void observeHadErrorSigningUp()  {
        mViewModel.getHadErrorSigningUp().observe(this, errorMessage -> {
            mViewModel.setQueryFlag(false);
            hideProgressBar();
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mViewModel.initSignupLiveData();
        mCallback = null;
    }

    private void showProgressBar()  {
        mSignupLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar()  {
        mSignupLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}
