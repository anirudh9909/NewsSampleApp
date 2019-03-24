package com.example.anirudhbharti.newsapp.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;
@JsonObject
public class NewsResponseModel {

    public String status;
    public Integer totalResults;
    public List<Article> articles = null;


    

}
