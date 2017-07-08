package com.janeullah.apps.healthinspectionviewer.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.databinding.FragmentRestaurantViolationsBinding;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedViolation;
import com.janeullah.apps.healthinspectionviewer.services.FirebaseInitialization;
import com.janeullah.apps.healthinspectionviewer.viewholder.ViolationViewHolder;

import org.parceler.Parcels;

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
    private static final String ARG_RESTAURANT = "restaurant";
    private static final String ARG_VIOLATION_SEVERITY = "violation_severity";

    private static final String TAG = "ViolationFragment";

    protected RecyclerView mRecycler;
    private FlattenedRestaurant restaurant;
    private DatabaseReference negaRestaurantsDatabaseReference;
    private FirebaseRecyclerAdapter<FlattenedViolation, ViolationViewHolder> mAdapter;

    public ViolationFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ViolationFragment newInstance(int sectionNumber, FlattenedRestaurant restaurantSelected) {
        ViolationFragment fragment = new ViolationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(ARG_RESTAURANT, Parcels.wrap(restaurantSelected));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRestaurantViolationsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant_violations, container, false);
        View rootView = binding.getRoot();
        restaurant = Parcels.unwrap(getArguments().getParcelable(ARG_RESTAURANT));
        binding.setRestaurantSelected(restaurant);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.violationsInRestaurant);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);

        negaRestaurantsDatabaseReference = FirebaseInitialization.getInstance()
                .getNegaDatabaseReference()
                .child("violations");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Query violationsQuery = negaRestaurantsDatabaseReference
                .child(restaurant.getNameKey())
                .child("violations")
                .orderByChild("severity")
                .equalTo(getArgViolationSeverity());

        mAdapter = new FirebaseRecyclerAdapter<FlattenedViolation, ViolationViewHolder>(FlattenedViolation.class, R.layout.item_flattenedviolation,
                ViolationViewHolder.class, violationsQuery) {

            @Override
            protected void populateViewHolder(ViolationViewHolder viewHolder, FlattenedViolation model, int position) {
                final DatabaseReference violationRef = getRef(position);
                final String violationRefKey = violationRef.getKey();
                Log.v(TAG, "Key: " + violationRefKey);
                viewHolder.bindData(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    private String getArgViolationSeverity(){
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        if (sectionNumber == 1){
            return "CRITICAL";
        }
        return "NONCRITICAL";
    }

}
