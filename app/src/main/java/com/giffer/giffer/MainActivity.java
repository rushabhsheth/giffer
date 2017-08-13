package com.giffer.giffer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.giffer.giffer.NewsCard.VideoCardFragment;

public class MainActivity extends AppCompatActivity{

    public static String PACKAGE_NAME;

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private VideoCardFragment mFragmentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.activity_main);

        // time delay to hide actionBar
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                // DO DELAYED STUFF
                getSupportActionBar().hide();
            }
        }, 5000); // e.g. 5000 milliseconds

        //Lets Test this change 2
        //Archana made this change 3
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
        Intent intent = new Intent(this, AddNewsActivity.class);
        startActivity(intent);
    }
}
