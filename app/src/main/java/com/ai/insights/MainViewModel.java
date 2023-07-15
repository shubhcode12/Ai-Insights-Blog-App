package com.ai.insights;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Channel> articleListLive = null;
    private static String urlString = "https://latestaiinsights.blogspot.com/feeds/posts/default";
    private MutableLiveData<String> snackbar = new MutableLiveData<>();
    private MutableLiveData<List<Article>> filteredArticles = new MutableLiveData<>();

    public LiveData<Channel> getChannel() {
        if (articleListLive == null) {
            articleListLive = new MutableLiveData<>();
        }
        return articleListLive;
    }

    public void fetchFeed() {
        Parser parser = new Parser.Builder()
                .build();

        parser.onFinish(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(@NonNull Channel channel) {
                setChannel(channel);
                setFilteredArticles(channel.getArticles());
            }

            @Override
            public void onError(@NonNull Exception e) {
                setChannel(new Channel(null, null, null, null, null, null, new ArrayList<>(), null));
                e.printStackTrace();
                snackbar.postValue("An error has occurred. Please try again");
            }
        });

        parser.execute(urlString);
    }

    private void setChannel(Channel channel) {
        articleListLive.postValue(channel);
    }

    public void setFilteredArticles(List<Article> articles) {
        filteredArticles.postValue(articles);
    }

}
