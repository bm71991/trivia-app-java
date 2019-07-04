package com.bm.android.trivia.game;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class FirestoreRepository    {
    private final String TAG = "FirestoreRepository";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private final ArrayList<String> perfectScoresCollections = new ArrayList<>(
            Arrays.asList(
            "booksPerfectScores",
            "filmPerfectScores",
            "TVPerfectScores",
            "musicPerfectScores"
            ));

    public FirestoreRepository()    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /* used in UserAccessViewModel */
    public Task<Void> addUserToDb() {
        String userEmail = mAuth.getCurrentUser().getEmail();
        WriteBatch addUserBatch = db.batch();

        for (String collectionName : perfectScoresCollections) {
            DocumentReference newDocument = db.collection(collectionName)
                    .document(userEmail);
            addUserBatch.set(newDocument, new initialPerfectScoreData());
        }

        return addUserBatch.commit();
    }

    public Task<Void> removeUserFromDb()   {
        String userEmail = mAuth.getCurrentUser().getEmail();
        WriteBatch deleteUserBatch = db.batch();

        for (String collectionName : perfectScoresCollections)  {
            DocumentReference docToDelete = db.collection(collectionName)
                    .document(userEmail);
            deleteUserBatch.delete(docToDelete);
        }
        return deleteUserBatch.commit();
    }

    /* used in GameViewModel */
    public void incrementPerfectScoreCount(String category, String difficulty,
                                           MutableLiveData<Boolean> dbOperationWasSuccessful)  {
        String collectionName = category + "PerfectScores";
        String difficultyCountKey = difficulty + "Count";
        db.collection(collectionName).document(mAuth.getCurrentUser().getEmail())
                .update(difficultyCountKey, FieldValue.increment(1))
                .addOnCompleteListener(dbOperation -> {
                    if (dbOperation.isSuccessful())    {
                        dbOperationWasSuccessful.setValue(true);
                    } else {
                        dbOperationWasSuccessful.setValue(false);
                    }
                });
    }

    /* used in BestPlayersViewModel */
    public void getBestPlayers(String category, String difficulty,
                               MutableLiveData<List<BestPlayer>> bestPlayerData)    {
        String collectionName = category + "PerfectScores";
        String difficultyCountKey = difficulty + "Count";
        db.collection(collectionName)
                .orderBy(difficultyCountKey, Query.Direction.DESCENDING)
                .limit(3)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<BestPlayer> bestPlayers = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            BestPlayer player = document.toObject(BestPlayer.class);
                            player.setEmail(document.getId());
                            bestPlayers.add(player);
                        }
                        bestPlayerData.setValue(bestPlayers);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }


    class initialPerfectScoreData    {
        public int easyCount;
        public int mediumCount;
        public int hardCount;

        initialPerfectScoreData()    {
            easyCount = 0;
            mediumCount = 0;
            hardCount = 0;
        }
    }
}
