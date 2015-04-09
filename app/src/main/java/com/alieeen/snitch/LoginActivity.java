package com.alieeen.snitch;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.alieeen.snitch.adapter.LoginAdapter;
import com.alieeen.snitch.adapter.WalkthroughAdapter;
import com.alieeen.snitch.util.NonScrollableViewPager;

import org.androidannotations.annotations.EActivity;

@EActivity
public class LoginActivity extends ActionBarActivity {

    private LoginAdapter mAdapter;
    private NonScrollableViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAdapter = new LoginAdapter(getSupportFragmentManager());

        mPager = (NonScrollableViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPagingEnabled(false);



        final Button login_button = (Button) findViewById(R.id.button_login);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() == 0) {
                    //primeira página, login user/senha
                    if(tryLogin()) {
                        mPager.setCurrentItem(1, true);
                    }
                    login_button.setText("CONECTAR");
                }
                else if (mPager.getCurrentItem() == 1)  {
                    if (tryUrl()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private boolean tryUrl() {
        //TODO
        return true;

    }

    private boolean tryLogin() {
    //TODO
        if (validLogin()) {

        }

        return true;
    }

    private boolean validLogin() {
    //TODO
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
