package com.bm.android.trivia.user_access;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bm.android.trivia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    private Button mLoginButton;
    private ProgressBar mProgressBar;
    private LoginFragmentCallback mCallback;
    private FirebaseAuth mAuth;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    public interface LoginFragmentCallback {
        void onLoginSuccess();
    }

    public static LoginFragment newInstance()  {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

        mLoginButton.setOnClickListener(v -> {
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            mProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressBar.setVisibility(View.GONE);
                            if (task.isSuccessful())    {
                                /* if username/password is correct && they are email verified:*/
                                if (mAuth.getCurrentUser().isEmailVerified())   {
                                    mCallback.onLoginSuccess();
                                }   else {
                                    /*if username/password is correct && not email verified:*/
                                    mAuth.signOut();
                                    Toast.makeText(getContext(),
                                            getString(R.string.unverified_email),
                                            Toast.LENGTH_LONG).show();
                                }
                                /* every other error */
                            } else  {
                                Toast.makeText(getContext(), task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
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