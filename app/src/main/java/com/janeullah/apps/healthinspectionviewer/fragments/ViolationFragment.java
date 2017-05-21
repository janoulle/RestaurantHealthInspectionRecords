package com.janeullah.apps.healthinspectionviewer.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.databinding.FragmentRestaurantViolationsBinding;

/**
 * @author Jane Ullah
 * @date 5/20/2017.
 */

public class ViolationFragment  extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ViolationFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ViolationFragment newInstance(int sectionNumber) {
        ViolationFragment fragment = new ViolationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRestaurantViolationsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant_violations, container, false);

        //View rootView = inflater.inflate(R.layout.fragment_restaurant_violations, container, false);
        View rootView = binding.getRoot();
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
