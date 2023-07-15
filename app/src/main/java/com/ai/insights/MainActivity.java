package com.ai.insights;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private MainViewModel viewModel;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        initializeViews();
        setupRecyclerView();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        progressBar = findViewById(R.id.progressBar);

        observeChannel();
        setupSwipeRefreshLayout();

        if (isNetworkAvailable()) {
            viewModel.fetchFeed();
        } else {
            showNetworkErrorDialog();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initializeViews() {
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(this);
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    }

    private void observeChannel() {
        viewModel.getChannel().observe(this, channel -> {
            if (channel != null) {
                setTitle(channel.getTitle());
                mAdapter = new ArticleAdapter(channel.getArticles(), MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary_color, R.color.primary_color);
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mAdapter.getArticleList().clear();
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(true);
            viewModel.fetchFeed();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_message)
                .setTitle(R.string.alert_title)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_positive, (dialog, id) -> finish());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_privacy) {
            startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "" + query, Toast.LENGTH_SHORT).show();
        mAdapter.performSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.performSearch(newText);
        return false;
    }
}
