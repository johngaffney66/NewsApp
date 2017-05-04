package com.example.android.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by HAL on 5/2/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    NewsAdapter(Activity context, ArrayList<News> news) {

        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        String webTitle = currentNews.getWebTitle();
        TextView webTitleView = (TextView) convertView.findViewById(R.id.web_title);
        webTitleView.setText(webTitle);

        String sectionName = currentNews.getSectionName();
        TextView sectionNameView = (TextView) convertView.findViewById(R.id.section_name);
        sectionNameView.setText(sectionName);

        return convertView;
    }

}
