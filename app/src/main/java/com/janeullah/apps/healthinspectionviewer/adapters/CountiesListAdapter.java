package com.janeullah.apps.healthinspectionviewer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.viewholder.CountyViewHolder;

import com.janeullah.apps.healthinspectionviewer.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * https://guides.codepath.com/android/using-the-recyclerview
 *
 * @author Jane Ullah
 * @date 4/27/2017.
 */
public class CountiesListAdapter extends RecyclerView.Adapter<CountyViewHolder> {
    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private List<County> countyNames;

    public void add(County county) {
        int previousSize = countyNames.size();
        countyNames.add(county);
        notifyItemInserted(previousSize);
    }

    public void setContext(final Context context) {
        this.mContext = context;
    }

    public CountiesListAdapter() {
    }

    public CountiesListAdapter(final Context context) {
        this.mContext = context;
    }

    public void addAll(List<County> listOfCounties) {
        countyNames.addAll(listOfCounties);
    }

    public CountiesListAdapter(final Context context, DatabaseReference dbRef) {
        this.mContext = context;
        this.mDatabaseReference = dbRef;
        this.countyNames = new ArrayList<>();
    }

    @Override
    public CountyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View countyListView = inflater.inflate(R.layout.item_countylistview, parent, false);
        return new CountyViewHolder(countyListView);
    }

    @Override
    public void onBindViewHolder(CountyViewHolder viewHolder, int position) {
        County countySelected = countyNames.get(position);
        TextView textView = viewHolder.countyNameView;
        textView.setText(countySelected.getName());
    }

    @Override
    public int getItemCount() {
        return countyNames.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }

    public County getItem(int position) {
        return countyNames.get(position);
    }
}
