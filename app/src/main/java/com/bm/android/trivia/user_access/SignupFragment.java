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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SignupFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mUsernameEditText;
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
        mUsernameEditText = view.findViewById(R.id.username);
        mSignupButton = view.findViewById(R.id.signup_button);

        mSignupButton.setOnClickListener(v -> {
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            String username = mUsernameEditText.getText().toString();

            mProgressBar.setVisibility(ProgressBar.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        mProgressBar.setVisibility(ProgressBar.GONE);

                        if (task.isSuccessful())    {
                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        setUserName(username);
                                    });
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        return view;
    }

    private void setUserName(String username)  {
        UserProfileChangeRequest usernameUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
            mAuth.getCurrentUser().updateProfile(usernameUpdate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                /*must explicitly sign out or else
                  mAuth.getCurrentUser() != null
                */
                mAuth.signOut();
                mCallback.onSignupSuccess();
            }
        });
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }
}
