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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class DeleteAccountFragment extends Fragment {
    private UserAccessViewModel mViewModel;
    private Button deleteAccountButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private DeleteAccountCallback mCallback;
    private LinearLayout mLinearLayout;
    private ProgressBar mProgressBar;


    public interface DeleteAccountCallback   {
        void onDeleteAccount();
    }

    public static DeleteAccountFragment newInstance()  {
        return new DeleteAccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(UserAccessViewModel.class);
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (DeleteAccountCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate
                (R.layout.delete_account_fragment, container, false);
        deleteAccountButton = view.findViewById(R.id.delete_account_button);
        emailEditText = view.findViewById(R.id.email_input);
        passwordEditText = view.findViewById(R.id.password_input);
        mProgressBar = view.findViewById(R.id.delete_account_progressbar);
        mLinearLayout = view.findViewById(R.id.delete_account_linear_layout);

        if (mViewModel.isDeletingAccount()) {
            showProgressBar();
        }

        deleteAccountButton.setOnClickListener(v -> {
            showProgressBar();
            mViewModel.setDeletingAccountFlag(true);
            String emailText = emailEditText.getText().toString();
            String passwordText = passwordEditText.getText().toString();
            mViewModel.deleteAccount(emailText, passwordText);
        });

        mViewModel.getSuccessfulDeletingAccount().observe(this, deletionIsSuccessful -> {
            if (deletionIsSuccessful)   {
                mCallback.onDeleteAccount();
            }
        });

        mViewModel.getErrorDeletingAccount().observe(this, errorMessage -> {
            hideProgressBar();
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void showProgressBar()   {
        mLinearLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar()  {
        mLinearLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }
}
