package com.giffer.giffer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

/**
 * Created by archanaarunkumar on 8/14/17.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarprofile);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.activity_profilename);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        mSignOutButton = (Button) findViewById(R.id.profile_signoutButton);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("SignOut","Yes");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

}
