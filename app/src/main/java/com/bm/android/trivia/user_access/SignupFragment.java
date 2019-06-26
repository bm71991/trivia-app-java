package com.bm.android.trivia.user_access;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bm.android.trivia.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;

public class SignupFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignupButton;
    private SignupFragmentCallback mCallback;

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
        mSignupButton = view.findViewById(R.id.signup_button);

        mSignupButton.setOnClickListener(v -> {
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            mProgressBar.setVisibility(ProgressBar.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        mProgressBar.setVisibility(ProgressBar.GONE);

                        if (task.isSuccessful())    {
                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        /*must explicitly sign out or else
                                          mAuth.getCurrentUser() != null
                                         */
                                        mAuth.signOut();
                                        mCallback.onSignupSuccess();
                                    });
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        return view;
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }
}
