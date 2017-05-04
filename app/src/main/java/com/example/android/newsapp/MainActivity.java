package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

    private static String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?q=Trump&api-key=test";
    private static final int NEWS_LOADER_ID = 1;
    public static final String LOG_TAG = MainActivity.class.getName();
    private NewsAdapter mAdapter;
    private TextView mEmptyState;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ListView newsList = (ListView) findViewById(R.id.list);

        mEmptyState = (TextView) findViewById(R.id.empty_view);
        newsList.setEmptyView(mEmptyState);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsList.setAdapter(mAdapter);


        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);

            }

        });


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {

            // Update empty state with no connection error message
            mEmptyState.setText(R.string.no_connection);
        }
    }
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"Created Loader");
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        mSwipeRefreshLayout.setRefreshing(false);

        if (news != null && !news.isEmpty()) {
            mAdapter.clear();
            mAdapter.addAll(news);
        }
        mEmptyState.setText(R.string.no_news);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader){
        Log.v(LOG_TAG, "On Load Reset");
        mAdapter.clear();
    }

    public void onRefresh(){
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }
}
