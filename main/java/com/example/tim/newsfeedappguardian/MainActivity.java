package com.example.tim.newsfeedappguardian;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?";
    ListView articleListView;
    TextView noArticleText;
    ProgressBar progressBar;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleListView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress_bar);
        noArticleText = findViewById(R.id.empty_view);
        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        articleListView.setAdapter(adapter);
        articleListView.setEmptyView(noArticleText);

        // Do not forget to add the permissions in AndroidManifest.xml or else
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            noArticleText.setText(R.string.no_internet);
        }
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Article currentArticle = adapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        String TAG = "URI";

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        String sections = "";

        if (sections.endsWith("|")) {
            sections = sections.substring(0, sections.length() - 1) + "";
        }
        if (!sections.isEmpty()) {
            uriBuilder.appendQueryParameter("section", sections);
        }
        uriBuilder.appendQueryParameter("show-fields", "thumbnail,byline");
        uriBuilder.appendQueryParameter("api-key", "test");
        Log.d(TAG, "uriBuilder: " + uriBuilder.toString());
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        noArticleText.setText(R.string.no_article);
        // Clear the adapter of previous article data
        adapter.clear();

        // If there is a valid list of {@link article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
        getLoaderManager().restartLoader(0, null, this);
    }

}
