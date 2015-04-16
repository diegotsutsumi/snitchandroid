package com.alieeen.snitch.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alieeen.snitch.loginfragments.Login01Fragment;
import com.alieeen.snitch.loginfragments.Login02Fragment;
import com.alieeen.snitch.loginfragments.LoginPhoneFragment;
import com.alieeen.snitch.walkthrougfrags.Walkthrough01Fragment;
import com.alieeen.snitch.walkthrougfrags.Walkthrough02Fragment;
import com.alieeen.snitch.walkthrougfrags.Walkthrough03Fragment;

/**
 * Created by alinekborges on 09/04/15.
 */
public class LoginAdapter extends FragmentPagerAdapter {

    private boolean requirePhone;

    public LoginAdapter(FragmentManager fm, boolean requirePhone) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (!requirePhone) {
            switch (position) {
                case 0:
                    return Login01Fragment.newInstance();
                case 1:
                    return Login02Fragment.newInstance();
                default:
                    return Login01Fragment.newInstance();
            }
        } else {
            switch (position) {
                case 0:
                    return Login01Fragment.newInstance();
                case 1:
                    return LoginPhoneFragment.newInstance();
                case 2:
                    return Login02Fragment.newInstance();
                default:
                    return Login01Fragment.newInstance();
            }
        }
        //return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {

        if (requirePhone) {
            return 3;
        } else {
            return 2;
        }
    }

    /*
    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }*/
}

