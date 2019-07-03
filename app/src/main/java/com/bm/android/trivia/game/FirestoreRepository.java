package com.bm.android.trivia.game;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class FirestoreRepository    {
    private final String TAG = "FirestoreRepository";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MutableLiveData<Integer> userDocumentCreationCount;
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
        userDocumentCreationCount = new MutableLiveData<>();
    }

    /* used in UserAccessViewModel */
    public void addUserToDb(MutableLiveData<String> hadErrorSigningUp,
                            MutableLiveData<Boolean> isSuccessfulSigningUp)   {
        String userEmail = mAuth.getCurrentUser().getEmail();
        userDocumentCreationCount.setValue(0);

        for (String collectionName : perfectScoresCollections)  {
            DocumentReference newDocument = db.collection(collectionName)
                    .document(userEmail);

            newDocument.set(new initialPerfectScoreData()).addOnCompleteListener(task -> {
                if (task.isSuccessful())    {
                    incrementDocumentCreationCount();
                    if (allDocumentsAreCreated())   {
                        isSuccessfulSigningUp.setValue(true);
                        userDocumentCreationCount = new MutableLiveData<>();
                        mAuth.signOut();
                    }
                } else {
                    userDocumentCreationCount = new MutableLiveData<>();
                    mAuth.signOut();
                    hadErrorSigningUp.setValue("could not add user to the database");
                }
            });
        }
    }

    private void incrementDocumentCreationCount()  {
        Integer previousCreationCount = userDocumentCreationCount.getValue();
        userDocumentCreationCount.setValue(previousCreationCount + 1);
    }

    private boolean allDocumentsAreCreated() {
        return userDocumentCreationCount.getValue() == perfectScoresCollections.size();
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
