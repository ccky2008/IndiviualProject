package com.bignerdranch.android.fyp.utils;


import android.content.Context;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by David on 3/6/2017.
 */

public class FirebaseLab {
    private static FirebaseLab sFirebaseLab;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Firebase firebase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static FirebaseLab get() {
        if (sFirebaseLab == null) {
            sFirebaseLab = new FirebaseLab();
        }
        return sFirebaseLab;
    }

    public FirebaseLab() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser  =  FirebaseAuth.getInstance().getCurrentUser();
        firebase = new Firebase("https://fypproject-acd2d.firebaseio.com/");
    }

    public Firebase getFirebaseDatabaseLink() {
        return firebase;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

}
