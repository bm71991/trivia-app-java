package com.bm.android.trivia.user_access;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bm.android.trivia.R;

import androidx.fragment.app.Fragment;

public class DeletedAccountFragment extends Fragment {
    DeletedAccountCallback mCallback;

    public interface DeletedAccountCallback     {
        void toWelcomeFragment();
    }

    public static DeletedAccountFragment newInstance() {
        return new DeletedAccountFragment();
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (DeletedAccountCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate
                (R.layout.deleted_account_fragment, container, false);
        Button backToMainButton = view.findViewById(R.id.back_to_main_button);

        backToMainButton.setOnClickListener(v -> {
            mCallback.toWelcomeFragment();
        });

        return view;
    }

    @Override
    public void onDetach()  {
        super.onDetach();
        mCallback = null;
    }
}
