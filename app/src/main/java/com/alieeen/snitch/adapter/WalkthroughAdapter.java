package com.alieeen.snitch.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alieeen.snitch.walkthrougfrags.Walkthrough01Fragment;
import com.alieeen.snitch.walkthrougfrags.Walkthrough02Fragment;
import com.alieeen.snitch.walkthrougfrags.Walkthrough03Fragment;

/**
 * Created by alinekborges on 31/03/15.
 */
public class WalkthroughAdapter extends FragmentPagerAdapter {

    public WalkthroughAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return Walkthrough01Fragment.newInstance();
            case 1:
                return Walkthrough02Fragment.newInstance();
            case 2:
                return Walkthrough03Fragment.newInstance();
            default:
                return Walkthrough01Fragment.newInstance();
        }

        //return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return 3;
    }

    /*
    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }*/
}

