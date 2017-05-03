package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by HAL on 5/2/2017.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils(){}

    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error when making a url connection"
                        + urlConnection.getResponseCode());
            }
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem making a url connection", e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)){
            return null;
        }

        List<News> news = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject newsResults = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = newsResults.getJSONArray("results");

            for(int i = 0; i < newsArray.length(); i++){
                JSONObject currentNews =newsArray.getJSONObject(i);
                String webTitle = currentNews.getString("webTitle");
                Log.d(LOG_TAG, "grabbed title");
                String sectionName = currentNews.getString("sectionName");
                Log.d(LOG_TAG,"grabbed section name");
                String webUrl = currentNews.getString("webUrl");

                News newsItems = new News(webTitle, sectionName, webUrl);

                news.add(newsItems);
                Log.d(LOG_TAG, "Books added");

            }
        }catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return news;
    }

    public static List<News> fetchNewsData (String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP Request", e);
        }

        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }
}
