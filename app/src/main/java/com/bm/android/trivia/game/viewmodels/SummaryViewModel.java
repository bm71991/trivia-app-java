package com.bm.android.trivia.game.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/*used by SummaryFragment and for communication between GameFragment and SummaryFragment*/
public class SummaryViewModel extends AndroidViewModel {
    private double finalScore;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        finalScore = -1;
    }

    public void setFinalScore(double numberOfQuestions, double correctAnswerCount)    {
        finalScore = correctAnswerCount / numberOfQuestions;
    }

    public String getFinalScore()   {
        int percentageNumber = (int) Math.round(finalScore * 100);
        return percentageNumber + "%";
    }
}


    /*************************TEST*/

//                        FirebaseFirestore.getInstance().collection("perfectScoreCount")
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//@Override
//public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//        ArrayList<User> test = new ArrayList<>();
//        for (DocumentSnapshot q : queryDocumentSnapshots)   {
//            Log.i("test", "id: " + q.getId());
//            User user = q.toObject(User.class);
//            user.setId(q.getId());
//            test.add(user);
//        }
//
//
//
//
//        Log.i("test", "id: " + test.get(0).getId());
//    Log.i("test", "id: " + test.get(0).getEasy());
//        }
//
//        });

/*************************/
