package com.bignerdranch.android.fyp.network;

import android.content.Context;

import com.bignerdranch.android.fyp.utils.FirebaseLab;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by David on 4/1/2017.
 */

public class DataRemoval {

    private static final String TAG = "DataRemoval";
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    Firebase mFirebase;
    Context context;

    public DataRemoval() {
        this.context = context;
        mFirebaseAuth = FirebaseLab.get().getFirebaseAuth();
        mFirebaseUser  = FirebaseLab.get().getFirebaseUser();
        mFirebase = FirebaseLab.get().getFirebaseDatabaseLink();
    }

    public void RemoveToken() {
        String userId = UserLab.get().getUserId();
        final Firebase userTokenRef = mFirebase.child("user_token").child(userId);
        userTokenRef.removeValue();
    }
}
