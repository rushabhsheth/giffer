package com.giffer.giffer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.giffer.giffer.NewsCard.VideoCardFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    public static final int RC_SIGN_IN = 1;
    public static final int RC_PROFILE = 2;
    public static final int RC_SIGN_IN_ADDNEWS = 3;

    //Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mNewsDatabaseReference;
    private ChildEventListener mNewsChildEventListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mNewsPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.activity_main);

        // Initialize Firebase components
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mNewsDatabaseReference = mFirebaseDatabase.getReference().child("news");
        mNewsPhotosStorageReference = mFirebaseStorage.getReference().child("news_photos");
        mUser = null;

        // time delay to hide actionBar
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                // DO DELAYED STUFF
                getSupportActionBar().hide();
            }
        }, 5000); // e.g. 5000 milliseconds


        //Firebase Auth State Listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
//                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
//                    onSignedOutCleanup();
//                    startLoginFlow();
                }
            }
        };

    }

    public void startLoginFlow (int intId){
        AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                .setPermissions(Arrays.asList("public_profile","user_friends","email"))
                .build();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                facebookIdp,
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
        );

        // User is signed out
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .setProviders(providers)
                        .build(),
                intId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_ADDNEWS) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                mUser = mFirebaseAuth.getCurrentUser();
                startAddActivity();

            } else {
                if (resultCode == RESULT_CANCELED) {
                    // Sign in was canceled by the user, finish the activity
                    Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                }

//                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
//                    Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
//                    return;
//                }
//                Toast.makeText(this, R.string.unknown_sign_in_response, Toast.LENGTH_LONG).show();
            }
        }

        else if (requestCode == RC_PROFILE ) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("SignOut")) {
                    String mSignOut = data.getExtras().getString("SignOut");
                    Log.d(TAG, "Intent with RC_Profile. Sign Out? " + mSignOut);
                    if (mUser !=null){
                        AuthUI.getInstance().signOut(this);
                        Toast.makeText(this, "Signed out", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case (R.id.action_profile):
                startProfileActivity();
                return true;
            case (R.id.action_search):
                return true;
            case (R.id.action_sort):
                return true;
            case (R.id.action_add):
                startAddActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void startAddActivity() {
        if(mUser != null) {
            Intent intent = new Intent(this, AddNewsActivity.class);
            startActivity(intent);
        }
        else {
            Log.d(TAG, "User not logged in. Log in to Add News");
            startLoginFlow(RC_SIGN_IN_ADDNEWS);
        }

    }

    public void startProfileActivity() {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivityForResult(intent, RC_PROFILE);

    }


}
