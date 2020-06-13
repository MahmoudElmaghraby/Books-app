package com.elmaghraby.books;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mLoadingPrograss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingPrograss = findViewById(R.id.pb_loading);

        try {
            URL bookUrl = ApiUtils.buildUrl("cooking");
            new BooksQueryTask().execute(bookUrl);

        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

    }


    public class BooksQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;
            try {
                result = ApiUtils.getJson(searchURL);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tvResult = findViewById(R.id.textView);
            TextView tvError = findViewById(R.id.tv_error);
            mLoadingPrograss.setVisibility(View.INVISIBLE);
            if (result == null) {
                tvResult.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvResult.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
            }
            tvResult.setText(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPrograss.setVisibility(View.VISIBLE);
        }
    }


}
