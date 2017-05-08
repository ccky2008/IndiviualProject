package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Student;
import com.bignerdranch.android.fyp.utils.FirebaseLab;
import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by David on 3/6/2017.
 */

public class UserInfoFragment extends Fragment {

    private View v;
    private Student mStudent = null;

    private ImageView mUserIcon;
    private TextView mUserID;
    private TextView mUserName;
    private TextView mUserClass;
    private TextView mUserBirth;
    private TextView mUserTelephone;
    private Button mResetButton;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    Firebase mFirebase;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //new FetchUserInfo(getContext()).execute();
        v = inflater.inflate(R.layout.home_user_info, container, false);
        getFirebaseInfo();
        mStudent = null;
        initUI();
        return v;
    }

    private void getFirebaseInfo() {
        mFirebase = FirebaseLab.get().getFirebaseDatabaseLink();
        mFirebaseAuth = FirebaseLab.get().getFirebaseAuth();
        mFirebaseUser = FirebaseLab.get().getFirebaseUser();
    }

    private void initUI() {
        mUserIcon = (ImageView) v.findViewById(R.id.userIcon);
        mUserID = (TextView) v.findViewById(R.id.userID);
        mUserName = (TextView) v.findViewById(R.id.userName);
        mUserClass = (TextView) v.findViewById(R.id.userClass);
        mUserBirth = (TextView) v.findViewById(R.id.userBirth);
        mUserTelephone = (TextView) v.findViewById(R.id.userTelephone);
        mResetButton = (Button) v.findViewById(R.id.reset_password);

        mUserID.setText(mStudent.getId());
        mUserName.setText(mStudent.getName());
        mUserClass.setText(mStudent.getClassID());
        mUserBirth.setText(mStudent.getBirth());
        mUserTelephone.setText(mStudent.getTelephone());

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = QueryPreferences.getStoredEmail(getContext());
                mFirebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
