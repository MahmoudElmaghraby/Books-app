package com.elmaghraby.books;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiUtils {

    private ApiUtils(){}

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";
    public static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyBITwbiSGgJCKR2mqg80aapFc9Fxhf_dc0";


    public static URL buildUrl(String title) {

        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY , API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }


/*

     // To build the full url that will return json data

    public static URL buildUrl(String title){
        String fullUrl = BASE_API_URL + "?q=" + title ;
        URL url = null;
        try {
            url = new URL(fullUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }*/


    public static String getJson(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (Exception e){
            Log.d("Error", e.toString());
            return null;
        }
        finally {
            connection.disconnect();
        }
    }



  /*  *//**
     * to get all the json data as a string from the url
     * @param url
     * @return
     * @throws IOException
     *//*
    public static String getJson(URL url) throws IOException{

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
           // InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A"); //To convert json to string, and this "\\A" regular expression mean to get all the data
            boolean hasData = scanner.hasNext();
            if (hasData)
                return scanner.next();
            else
                return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            connection.disconnect();
        }

    }*/


}
