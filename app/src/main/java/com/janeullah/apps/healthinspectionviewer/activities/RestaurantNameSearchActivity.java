package com.janeullah.apps.healthinspectionviewer.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.janeullah.apps.healthinspectionviewer.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantNameSearchActivity extends BaseActivity {
    private static final String TAG = "RestaurantSearch";

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_name_search);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // Get the intent, verify the action and get the query
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            loadActivity(this, AboutActivity.class);
            return true;
        } else if(id == R.id.action_legal){
            loadActivity(this, LegalActivity.class);
            return true;
        }else if (id == android.R.id.home) {
            Log.i(TAG, "Up clicked!");
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            navigateUp(this,upIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showProgressDialog(String.format(Locale.getDefault(),"Loading restaurants for query %s", query));
        }
    }
}
