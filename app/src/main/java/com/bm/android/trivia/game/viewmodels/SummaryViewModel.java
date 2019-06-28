package com.bm.android.trivia.game;

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
//    FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
//
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    Map<String, Object> user = new HashMap<>();
//        user.put("email", fbUser.getEmail());
//                user.put("easy", 0);
//                DocumentReference docRef = db.collection("perfectScoreCount/books/userScores").document(fbUser.getEmail());
//                docRef.set(user);
//                db.collection("perfectScoreCount/books/userScores").orderBy("easy", Query.Direction.DESCENDING)
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//@Override
//public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//        ArrayList<User> test = new ArrayList<>();
//        for (DocumentSnapshot q : queryDocumentSnapshots)
//        test.add(q.toObject(User.class));
//
//        Log.i("test", "email: " + test.get(0).getEmail() +
//        ", easy perfect scores = " +test.get(0).getEasy());
//        }
//
//        });

/*************************/
