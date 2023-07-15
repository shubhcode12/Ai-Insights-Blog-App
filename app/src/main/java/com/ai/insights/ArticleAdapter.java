package com.ai.insights;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;
    private List<Article> filteredArticles;
    private Context mContext;

    public List<Article> getArticleList() {
        return articles;
    }

    public ArticleAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.filteredArticles = new ArrayList<>(articles);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = filteredArticles.get(position);
        // Bind article data to views in ViewHolder
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getDescription());
        holder.author.setText("BY " + article.getAuthor());
        holder.pubDate.setText(formatDate(article.getPubDate()));
        holder.category.setText(getFirstCategory(article.getCategories()));

        Picasso.get()
                .load(article.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            // Handle item click
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("content", article.getContent());
            intent.putExtra("image", article.getImage());
            intent.putExtra("date", article.getPubDate());
            intent.putExtra("author", article.getAuthor());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredArticles.size();
    }

    public void performSearch(String query) {
        filteredArticles.clear();
        if (query.isEmpty()) {
            filteredArticles.addAll(articles);
        } else {
            query = query.toLowerCase();
            for (Article article : articles) {
                if (article.getTitle().toLowerCase().contains(query)) {
                    filteredArticles.add(article);
                }
            }
        }
        notifyDataSetChanged();
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat sourceSdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
            Date date = sourceSdf.parse(dateString);
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
                return sdf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    private String getFirstCategory(List<String> categories) {
        if (!categories.isEmpty()) {
            return categories.get(0);
        }
        return "";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView author;
        TextView pubDate;
        TextView category;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            author = itemView.findViewById(R.id.author);
            pubDate = itemView.findViewById(R.id.pubDate);
            category = itemView.findViewById(R.id.categories);
            image = itemView.findViewById(R.id.image);
        }
    }
}
