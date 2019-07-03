package com.bm.android.trivia.game;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.android.trivia.R;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BestPlayersAdapter extends
        RecyclerView.Adapter<BestPlayersAdapter.PlayerViewHolder> {
    private List<BestPlayer> mDataset;
    private String mCategory;
    private String mDifficulty;

    public static class PlayerViewHolder extends RecyclerView.ViewHolder    {
        TextView playerTextView;

        public PlayerViewHolder(TextView v)    {
            super(v);
            playerTextView = v;
        }
    }

    public BestPlayersAdapter(List<BestPlayer> myDataset, String category, String difficulty) {
         mDataset = myDataset;
         mCategory = category;
         mDifficulty = difficulty;
    }

    @Override
    public BestPlayersAdapter.PlayerViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.best_player_item, parent, false);

        PlayerViewHolder vh = new PlayerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        BestPlayer playerToDisplay = mDataset.get(position);
        String playerEmail = playerToDisplay.email;
        int playerWinCount = getPlayerWinCount(playerToDisplay);
        int itemNumber = position + 1;
        String textToDisplay =
                itemNumber + ". " + playerEmail + ": " + playerWinCount + " perfect scores";
        holder.playerTextView.setText(textToDisplay);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private int getPlayerWinCount(BestPlayer playerToDisplay)   {
        int winCount = -1;
        switch (mDifficulty)    {
            case ("easy"):
                winCount = playerToDisplay.easyCount;
                break;
            case ("medium"):
                winCount = playerToDisplay.mediumCount;
                break;
            case ("hard"):
                winCount = playerToDisplay.hardCount;
                break;
        }
        return winCount;
    }


}
