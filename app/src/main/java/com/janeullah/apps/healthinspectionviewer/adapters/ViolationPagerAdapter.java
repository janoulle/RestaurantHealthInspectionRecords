package com.janeullah.apps.healthinspectionviewer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.janeullah.apps.healthinspectionviewer.fragments.ViolationFragment;

/* A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * @author Jane Ullah
 * @date 5/20/2017.
 */

public class ViolationPagerAdapter extends FragmentPagerAdapter {

    public ViolationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ViolationFragment (defined as a static inner class below).
        return ViolationFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Critical Violations";
        } else if (position == 1) {
            return "Non-Critical Violations";
        }
        return null;
    }
}