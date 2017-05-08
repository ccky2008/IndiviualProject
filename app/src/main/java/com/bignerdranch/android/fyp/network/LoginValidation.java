package com.bignerdranch.android.fyp.network;

import android.util.Log;

import com.bignerdranch.android.fyp.models.Email;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/1/2017.
 */

public class LoginValidation {

    private static final String TAG = "LoginValidation";
    private Firebase mFirebase;
    List<Email> emailListStudent;
    List<Email> emailListTeacher;

    public LoginValidation() {
    }

    public void initEmailStudents() {
        mFirebase = new Firebase("https://fypproject-acd2d.firebaseio.com/studentemails");
        emailListStudent = new ArrayList<Email>();
        getListOfEmail(mFirebase, emailListStudent);
    }

    public void initEmailTeachers() {
        mFirebase = new Firebase("https://fypproject-acd2d.firebaseio.com/teacheremails");
        emailListTeacher = new ArrayList<Email>();
        getListOfEmail(mFirebase, emailListTeacher);
    }

    // should and get teachers email
    private void getListOfEmail(Firebase firebase, final List<Email> emailList) {
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot emailSnapshot: dataSnapshot.getChildren()) {
                    Email email = emailSnapshot.getValue(Email.class);
                    //Log.i(TAG, email.getEmail());
                    emailList.add(email);

                }
                //Log.i("TAG", "Firebase is executed");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public List getEmailsStudent() {
        return emailListStudent;
    }

    public List getEmailsTeacher() {
        return emailListTeacher;
    }
}
