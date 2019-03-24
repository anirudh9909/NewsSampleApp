package com.example.anirudhbharti.newsapp.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class Article implements Serializable {

    @JsonField(name = "source")
    public SourceBean source;
    @JsonField(name = "author")
    public String author;
    @JsonField(name = "title")
    public String title;
    @JsonField(name = "description")
    public String description;
    @JsonField(name = "url")
    public String url;
    @JsonField(name = "urlToImage")
    public String urlToImage;
    @JsonField(name = "publishedAt")
    public String publishedAt;
    @JsonField(name = "content")
    public String content;

    @JsonObject
    public static class SourceBean {
        @JsonField(name = "id")
        public String id;
        @JsonField(name = "name")
        public String name;
    }


}

