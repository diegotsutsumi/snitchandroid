package com.alieeen.snitch;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.alieeen.snitch.util.SharedPrefs;


public class SplashScreen extends ActionBarActivity implements Runnable{

    private int sleep_time = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Handler h = new Handler();
        h.postDelayed(this, sleep_time);



    }

    private void setUpActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.hide();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {

        boolean isLoggedIn = SharedPrefs.isLoggedIn(this);

        Intent i;

        if (isLoggedIn) {
            i = new Intent(this, MainActivity_.class);
        } else {
            i = new Intent(this, WalkthroughActivity_.class);
        }
        startActivity(i);
        finish();
    }
}