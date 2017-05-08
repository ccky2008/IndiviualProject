package com.bignerdranch.android.fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bignerdranch.android.fyp.utils.SharedMethod;
import com.bignerdranch.android.fyp.utils.WifiValidation;
import com.bignerdranch.android.fyp.fragments.AlertDialogFragment;
import com.bignerdranch.android.fyp.models.Email;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.network.LoginValidation;
import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.bignerdranch.android.fyp.R;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class LoginActivity extends AppCompatActivity implements AlertDialogFragment.OnDataPassForLogout{
   //implements BackgroundWorker.AsyncResponse

    private static final String TAG = "LoginActivity";
    private static final String ARG_FRAGMENT = "fragment";
    private static final String DIALOG_NOTIFICATION = "DialogNotification";
    private Button mSignIn_Button;
    private EditText mUserEmail_View;
    private EditText mPassword_View;
    private View focusView = null;
    private CircularProgressBar mCircularProgressBar;
    private String email;
    private boolean isTeacher = false;
    private boolean isStudent = false;
    private LoginValidation mLoginValidation;

    private Boolean authFlag = false;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // new FetchEmail().execute();
        getFirebaseData();
        setContentView(R.layout.login_main);
        initUI();
        hasLoginBefore();

        checkAuth();
    }

    private void checkAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (authFlag == false) {
                        updateUI();
                        getUserData();
                        authFlag = true;
                    }
                }
            }
        };
    }

    private void getFirebaseData() {
        mLoginValidation = new LoginValidation();
        mLoginValidation.initEmailStudents();
        mLoginValidation.initEmailTeachers();
    }

    private void initUI() {
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        mCircularProgressBar.setVisibility(View.GONE);
        mUserEmail_View = (EditText) findViewById(R.id.studentID);

        mPassword_View = (EditText) findViewById(R.id.password);
        mPassword_View.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Log.i(TAG,"what is this");
                }
                return false;
            }
        });

        mSignIn_Button = (Button)findViewById(R.id.sign_in_button);
        mSignIn_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mUserEmail_View.getText().toString();
                if (validateForm()) {
                    hideSoftKeyboard();
                    updateUI();
                    isValidUser();
                }
            }
        });
    }

    private void isValidUser() {
        if(setValidUser()) {
            QueryPreferences.setStoredEmail(getApplicationContext(), email);
            signIn(mUserEmail_View.getText().toString().trim(), mPassword_View.getText().toString().trim());
        } else {
            Log.i(TAG, "Invalid");
            showErrorDialogToUser();
        }
    }
    private void hasLoginBefore() {
        String email = QueryPreferences.getStoredEmail(this);
        if (email != null) {
            mUserEmail_View.setText(email);
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           checkAuth();
                        }

                        if (!task.isSuccessful()) {
                            showErrorDialogToUser();
                        }
                    }
                });
    }


    private void updateUI() {
        mUserEmail_View.setHint("");
        mPassword_View.setHint("");
        mUserEmail_View.setVisibility(View.GONE);
        mPassword_View.setVisibility(View.GONE);
        mSignIn_Button.setVisibility(View.GONE);
        mCircularProgressBar.setVisibility(View.VISIBLE);
    }

    private void redisplayUI() {
        mCircularProgressBar.setVisibility(View.GONE);
        mUserEmail_View.setHint(R.string.prompt_email);
        mPassword_View.setHint(R.string.prompt_password);
        mUserEmail_View.setVisibility(View.VISIBLE);
        mPassword_View.setVisibility(View.VISIBLE);
        mSignIn_Button.setVisibility(View.VISIBLE);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mUserEmail_View.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mUserEmail_View.setError("Required.");
            valid = false;
        } else {
            mUserEmail_View.setError(null);
        }

        String password = mPassword_View.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword_View.setError("Required.");
            valid = false;
        } else {
            mPassword_View.setError(null);
        }
        return valid;
    }

    private void showErrorDialogToUser() {
        //Toast.makeText(this, "Invalid User", Toast.LENGTH_LONG);
        FragmentManager manager = getSupportFragmentManager();
        AlertDialogFragment dialog = AlertDialogFragment
                .newInstance("Error", 1);
        dialog.show(manager, DIALOG_NOTIFICATION);
        redisplayUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        focusView = getCurrentFocus();
        if (focusView!= null) focusView.clearFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean setValidUser() {
        isStudent = isStudentEmail();
        QueryPreferences.setIsStudent(this,isStudent);
        if (!isStudent) {
            isTeacher = isTeacherEmail();
        }

        if (isStudent || isTeacher) {
            return true;
        }
        return false;
    }

    private boolean isStudentEmail() {
        List<Email> emailListStudent = mLoginValidation.getEmailsStudent();
        for (int i = 0 ; i < emailListStudent.size(); i++) {
            if (emailListStudent.get(i).getEmail().equals(email)) {
                //Log.i(TAG, emailListStudent.get(i).getEmail() + "   x: x  " + email);
                return true;
            }
        }
        return false;
    }

    private boolean isTeacherEmail() {
        List<Email> emailListTeacher = mLoginValidation.getEmailsTeacher();
        for(int i = 0; i < emailListTeacher.size() ; i++) {
            if (emailListTeacher.get(i).getEmail().equals(email)) {
                //Log.i(TAG, emailListTeacher.get(i).getEmail() + "  x  :  x  " + email);
                return true;
            }
        }
        return false;
    }

    private void getUserData() {
       // DataRetrievalUser user = new DataRetrievalUser(getApplicationContext());
        new DataRetrievalUser(getApplicationContext()).getUserInfo(new DataRetrievalUser.OnGetDataListener() {
            @Override
            public void onStart() {
                //Log.i(TAG, "The method is start");
            }

            @Override
            public void onSuccess(boolean userLogin) {
                SharedMethod.sendToken();
                if (userLogin) {
                    getMacAddress();
                }
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    private void getMacAddress() {
        new DataRetrievalUser(getApplicationContext()).getMacAddress(new DataRetrievalUser.OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(boolean value) {
                login();
                saveMacAddress(value);
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    private void saveMacAddress(boolean isMacAddressExist) {
        if (!QueryPreferences.getIsFirstTimeLogin(getApplicationContext()) && !isMacAddressExist) {
            QueryPreferences.setIsFirstTimeLogin(getApplicationContext(),true);
            new DataSaving().saveMacAddress(WifiValidation.getMacAddr());
        }
    }

    private void login() {
        boolean loginUser = QueryPreferences.getIsStudent(this);
        Intent i= null;
        finish();
        if (loginUser) {
            i = new Intent(getApplicationContext(), MainPageActivity.class);
        } else if(!loginUser) {
            i = new Intent(getApplicationContext(), TeacherMainPageActivity.class);
        }
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onDataPassForLogout(Boolean logout){
        redisplayUI();
    }


}
