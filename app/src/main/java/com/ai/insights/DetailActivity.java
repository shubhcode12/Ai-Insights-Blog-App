package com.ai.insights;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.ai.insights.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    WebView articleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        articleView = binding.webViewContent;

        Intent intent = getIntent();

        if (intent != null) {
            String postTitle = intent.getStringExtra("title");
            String postCategory = intent.getStringExtra("category");
            String postDate = intent.getStringExtra("date");
            String image = intent.getStringExtra("image");
            String content = intent.getStringExtra("content");

            binding.textViewTitle.setText(postTitle);
            binding.textViewDate.setText(postDate);
            binding.textViewCategory.setText(postCategory);

            WebSettings webSettings = articleView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);

            articleView.setHorizontalScrollBarEnabled(false);
            articleView.setWebChromeClient(new WebChromeClient());

            // Apply CSS styles to make the WebView responsive
            String cssStyle = "<style>body{margin:0;padding:0;width:100%;height:auto;} " +
                    "img{display: inline; height: auto; max-width: 100%;}" +
                    "iframe{ height: auto; width: 100%;}" +
                    "</style>";

            String htmlContent = "<html><head>" + cssStyle + "</head><body>" + content + "</body></html>";

            articleView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
