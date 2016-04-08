package com.methodica.lizalinto.dummyinbox;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<NewsItem> ITEMS = new ArrayList<NewsItem>();

    public static final Map<String, NewsItem> ITEM_MAP = new HashMap<String, NewsItem>();


    public static void addItem(String id, String title, String content, String publisher, String publishedDate, String link,String imageUrl, Bitmap imageBitmap) {
       NewsItem newsItem = new NewsItem(id, title, content, publisher, publishedDate, link, imageUrl, imageBitmap);
        ITEMS.add(newsItem);
        ITEM_MAP.put(newsItem.id, newsItem);
    }
    
    /**
     * A dummy item representing a piece of content.
     */
    public static class NewsItem {
        public final String id;
        public final String title;
        public final String content;
        public final String publisher;
        public final String publishedDate;
        public final String link;
        public final String imageUrl;
        public Bitmap imageBM;

        public void setImage(Bitmap imageBitmap) {
            this.imageBM = imageBitmap;
        }

        public NewsItem(String id, String title, String content, String publisher, String publishedDate, String link, String imageUrl, Bitmap imageBitmap) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.publisher = publisher;
            this.publishedDate = publishedDate;
            this.link = link;
            this.imageUrl = imageUrl;
            this.imageBM = imageBitmap;

        }


    }
}
