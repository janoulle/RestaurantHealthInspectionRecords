package com.janeullah.apps.healthinspectionviewer.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.fragments.ViolationFragment;

/* A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * @author Jane Ullah
 * @date 5/20/2017.
 */
public class ViolationPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public ViolationPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        context = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ViolationFragment
        return ViolationFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.critical_violations_tab);
        } else if (position == 1) {
            return context.getString(R.string.non_critical_violations_tab);
        }
        return null;
    }
}