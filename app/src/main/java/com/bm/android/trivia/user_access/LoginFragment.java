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
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginFragment extends Fragment {
    private Button mLoginButton;
    private ProgressBar mProgressBar;
    private LoginFragmentCallback mCallback;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private UserAccessViewModel mViewModel;
    private LiveData<Boolean> isEmailVerified;
    private LiveData<String> hadErrorSigningIn;
    private LinearLayout mLoginLayout;

    public interface LoginFragmentCallback {
        void onLoginSuccess();
    }

    public static LoginFragment newInstance()  {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(UserAccessViewModel.class);
        isEmailVerified = mViewModel.getIsEmailVerified();
        hadErrorSigningIn = mViewModel.getHadErrorSigningIn();
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (LoginFragmentCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mLoginButton = view.findViewById(R.id.login_button);
        mProgressBar = view.findViewById(R.id.auth_progress_bar);
        mEmailEditText = view.findViewById(R.id.email_input);
        mPasswordEditText = view.findViewById(R.id.password_input);
        mLoginLayout = view.findViewById(R.id.login_linear_layout);

        /*if firebase is still querying for user login authentication after an orientation change,
          display the progressbar
         */
        if (mViewModel.isQuerying())    {
            showProgressBar();
        }

        mLoginButton.setOnClickListener(v -> {
            mViewModel.setQueryFlag(true);
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            showProgressBar();
            mViewModel.signIn(email, password);
        });

        observeIsEmailedVerified();
        observeHadErrorSigningIn();

        return view;
    }

    private void observeIsEmailedVerified() {
        isEmailVerified.observe(this, emailVerified -> {
            /*firebase is done querying about user auth */
            mViewModel.setQueryFlag(false);
            if (emailVerified) {
                //reset viewmodel mutablelivedata
                mCallback.onLoginSuccess();
            } else {
                hideProgressBar();
                Toast.makeText(LoginFragment.this.getContext(),
                        LoginFragment.this.getString(R.string.unverified_email),
                        Toast.LENGTH_LONG).show();
            }

        });
    }

    private void observeHadErrorSigningIn() {
        hadErrorSigningIn.observe(this, errorMessage -> {
            mViewModel.setQueryFlag(false);
            hideProgressBar();
            Toast.makeText(getContext(),
                    errorMessage,
                    Toast.LENGTH_LONG).show();
        });
    }

    private void showProgressBar() {
            mLoginLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar()    {
        mLoginLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }
}