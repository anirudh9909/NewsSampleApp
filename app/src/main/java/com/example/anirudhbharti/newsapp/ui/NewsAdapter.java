package com.example.anirudhbharti.newsapp.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anirudhbharti.newsapp.ImageLoader;
import com.example.anirudhbharti.newsapp.R;
import com.example.anirudhbharti.newsapp.model.Article;
import com.example.anirudhbharti.newsapp.di.qualifier.ActivityContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    @Inject
    @ActivityContext
    public Context activityContext;
    private Date date = null;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
    private List<Article> newsList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private ImageLoader imgLoader;


    @Inject
    NewsAdapter() {

    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    void addItem(List<Article> newsList) {
        if (newsList != null && newsList.size() > 0) {
            this.newsList.clear();
            this.newsList = newsList;
            imgLoader = new ImageLoader(activityContext);
            notifyDataSetChanged();

        }

    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        if (newsList.get(position).title != null) {
            holder.tvTitle.setText(newsList.get(position).title);
            holder.tvTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        if (newsList.get(position).description != null) {
            holder.tvDescription.setText(newsList.get(position).description);
            holder.tvDescription.setVisibility(View.VISIBLE);
        } else {
            holder.tvDescription.setVisibility(View.GONE);
        }
        try {
            if (newsList.get(position).publishedAt != null && !newsList.get(position).toString().isEmpty()) {
                date = inputFormat.parse(newsList.get(position).publishedAt);
                String formattedDate = outputFormat.format(date);
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvDate.setText(formattedDate);
            } else {
                holder.tvDate.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsList.get(position).url != null) {
                    onItemClickListener.onItemClick(newsList.get(position).url);
                }
            }
        });


        if (newsList.get(position).urlToImage != null && !newsList.get(position).urlToImage.isEmpty()) {
            imgLoader.DisplayImage(newsList.get(position).urlToImage, holder.ivNews);
        }


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    interface OnItemClickListener {
        void onItemClick(String url);
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivNews)
        ImageView ivNews;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.cvParent)
        CardView cvParent;

        NewsViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}
