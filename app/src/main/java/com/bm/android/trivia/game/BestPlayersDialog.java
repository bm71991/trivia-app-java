package com.bm.android.trivia.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.bm.android.trivia.R;
import com.bm.android.trivia.game.viewmodels.BestPlayersViewModel;

import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BestPlayersDialog extends DialogFragment {
    private BestPlayersViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LiveData<List<BestPlayer>> mBestPlayers;
    private LinearLayoutManager mLinearLayoutManager;
    private String mTitle;
    private BestPlayersAdapter mAdapter;
    private String mCategoryChosen;
    private String mDifficultyChosen;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)    {
        mViewModel = ViewModelProviders.of(getActivity()).get(BestPlayersViewModel.class);
        mCategoryChosen = mViewModel.getCategoryStringChosen();
        mDifficultyChosen = mViewModel.getDifficultyStringChosen();
        mBestPlayers = mViewModel.getBestPlayers();
        mTitle = getString(R.string.best_players_dialog_title,
                mCategoryChosen, mDifficultyChosen);
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.best_players_dialog, null);
        mRecyclerView = v.findViewById(R.id.best_players_recyclerview);
        mProgressBar = v.findViewById(R.id.best_players_progressbar);


        if (!mViewModel.hasCalledDb())  {
            mViewModel.getTopPlayers();
        }
        if (mViewModel.isLoadingResults())  {
            showProgressBar();
            mBestPlayers.observe(this, bestPlayers -> {
                configRecyclerView(bestPlayers);
                hideProgressBar();
                mViewModel.setIsLoadingResults(false);
            });
        } else {
            configRecyclerView(mViewModel.getBestPlayers().getValue());
            hideProgressBar();
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(mTitle)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.setHasCalledDb(false);
                    }
                }).create();
    }

    private void showProgressBar()  {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void configRecyclerView(List<BestPlayer> dataset)    {
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new BestPlayersAdapter(dataset, mCategoryChosen, mDifficultyChosen);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void hideProgressBar()  {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public static BestPlayersDialog newInstance()  {
        return new BestPlayersDialog();
    }
}
