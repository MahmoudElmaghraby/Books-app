package com.elmaghraby.books;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtils {

    private ApiUtils() {
    }

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";
    public static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyBITwbiSGgJCKR2mqg80aapFc9Fxhf_dc0";
    public static final String TITLE = "intitle:";
    public static final String AUTHOR = "inauthor:";
    public static final String PUBLISHER = "inpublisher:";
    public static final String ISBN = "isbn:";


    public static URL buildUrl(String title) {

        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(String title, String author, String publisher, String isbn) {
        URL url = null;
        StringBuilder sb = new StringBuilder();

        if (!title.isEmpty()) sb.append(TITLE + title + "+");
        if (!author.isEmpty()) sb.append(AUTHOR + author + "+");
        if (!publisher.isEmpty()) sb.append(PUBLISHER + publisher + "+");
        if (!isbn.isEmpty()) sb.append(ISBN + isbn + "+");
        //removes the last character
        sb.setLength(sb.length() - 1);
        String query = sb.toString();
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
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
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        } finally {
            connection.disconnect();
        }
    }



  /*
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


    public static ArrayList<Book> getBooksFromJson(String json) {

        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHER_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGE_LINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numOfBooks = arrayBooks.length();
            for (int i = 0; i < numOfBooks; i++) {
                JSONObject bookJson = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO);

                JSONObject imageLinksJSON = null;
                if (volumeInfoJson.has(IMAGE_LINKS)) {
                    imageLinksJSON = volumeInfoJson.getJSONObject(IMAGE_LINKS);
                }

                int authorNum;
                try {
                    authorNum = volumeInfoJson.getJSONArray(AUTHORS).length();
                } catch (Exception e) {
                    authorNum = 0;
                }

                String[] authors = new String[authorNum];
                for (int j = 0; j < authorNum; j++) {
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(
                        bookJson.getString(ID),
                        volumeInfoJson.getString(TITLE),
                        (volumeInfoJson.isNull(SUBTITLE)) ? "" : volumeInfoJson.getString(SUBTITLE),
                        authors,
                        (volumeInfoJson.isNull(SUBTITLE)) ? "" : volumeInfoJson.getString(PUBLISHER),
                        (volumeInfoJson.isNull(SUBTITLE)) ? "" : volumeInfoJson.getString(PUBLISHER_DATE),
                        (volumeInfoJson.isNull(SUBTITLE)) ? "" : volumeInfoJson.getString(DESCRIPTION),
                        (imageLinksJSON == null) ? "" : imageLinksJSON.getString(THUMBNAIL)
                );
                books.add(book);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

}
