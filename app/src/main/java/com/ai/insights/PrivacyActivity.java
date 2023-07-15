package com.ai.insights;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebViewClient;

import com.ai.insights.databinding.ActivityPrivacyBinding;

public class PrivacyActivity extends AppCompatActivity {

    private ActivityPrivacyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.webviewPrivacy.getSettings().setJavaScriptEnabled(true);
        binding.webviewPrivacy.setHorizontalScrollBarEnabled(false);
        binding.webviewPrivacy.setWebViewClient(new WebViewClient()); // Add this line

        binding.webviewPrivacy.loadUrl(getResources().getString(R.string.privacy_policy_url));
    }


}