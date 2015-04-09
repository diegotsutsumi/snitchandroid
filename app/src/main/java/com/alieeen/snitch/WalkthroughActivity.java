package com.alieeen.snitch;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alieeen.snitch.adapter.WalkthroughAdapter;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.androidannotations.annotations.EActivity;

@EActivity
public class WalkthroughActivity extends ActionBarActivity {

    private WalkthroughAdapter mAdapter;
    private ViewPager mPager;
    private PageIndicator mIndicator;
    private View SkipButton;
    private View DoneButton;
    private View NextButton;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        SkipButton = findViewById(R.id.skip_button);
        DoneButton = findViewById(R.id.done_button);
        NextButton = findViewById(R.id.next_button);

        mAdapter = new WalkthroughAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);



        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

                if (position == 2) {
                    DoneButton.setVisibility(View.VISIBLE);
                    NextButton.setVisibility(View.INVISIBLE);
                } else {
                    DoneButton.setVisibility(View.INVISIBLE);
                    NextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndicator.setCurrentItem(currentPage + 1);
            }
        });

        SkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalkthroughActivity.this, LoginActivity_.class);
                startActivity(i);
            }
        });

        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalkthroughActivity.this, LoginActivity_.class);
                startActivity(i);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_walkthrough, menu);
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
