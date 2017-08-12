package com.giffer.giffer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity{

    public static String PACKAGE_NAME;

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private VideoCardFragment mFragmentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
