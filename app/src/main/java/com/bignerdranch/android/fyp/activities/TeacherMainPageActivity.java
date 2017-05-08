package com.bignerdranch.android.fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.utils.WifiValidation;
import com.bignerdranch.android.fyp.adapters.ImageAdapter;
import com.bignerdranch.android.fyp.network.DataRemoval;
import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by David on 3/4/2017.
 */

public class TeacherMainPageActivity extends AppCompatActivity {

    private static final String TAG = "TeacherMainPageActivity" ;
    public static final String key = "Key";
    private FirebaseAuth mFirebaseAuth;
    private Button mLogoutButton;
    private GridView mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        Log.i(TAG, UserLab.get().getUserId());
        setContentView(R.layout.teacher_home_content_main);


        mGridView = (GridView) findViewById(R.id.teacher_main_grid);
        mGridView.setAdapter(new ImageAdapter(this));
        mGridView.setOnItemClickListener(onItemClickGridView);

        mLogoutButton = (Button) findViewById(R.id.logout);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                new DataRemoval().RemoveToken();
                QueryPreferences.setIsStudent(getApplicationContext(), false);
                mFirebaseAuth.signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        Test();
    }

    private void Test() {
        WifiValidation x = new WifiValidation(getApplicationContext());
        Log.i(TAG, x.getTimeZone());
    }
    private AdapterView.OnItemClickListener onItemClickGridView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent(getApplicationContext(), TeacherContainerActivity.class);
            i.putExtra(key, position);
            startActivity(i);
        }
    } ;
}
