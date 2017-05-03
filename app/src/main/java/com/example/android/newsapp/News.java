package com.example.android.newsapp;

/**
 * Created by HAL on 5/2/2017.
 */

public class News {

    private String mWebTitle;
    private String mSectionName;
    private String mUrl;


    public News (String webTitle, String sectionName , String url) {
        mWebTitle = webTitle;
        mSectionName = sectionName;
        mUrl = url;
    }

    public String getWebTitle() {return mWebTitle;}

    public String getSectionName() {return mSectionName;}

    public String getUrl() {return mUrl;}



}
