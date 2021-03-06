package com.example.cgallen.asynctasks1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostArtistActivity extends AppCompatActivity implements OnClickListener {

    // note change from http://www.free-map.org.uk/course/mad/ws/hits.php
    private static final String BASE_URL = "http://www.free-map.org.uk/course/ws/hits.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_song);
        Button addSong = (Button) findViewById(R.id.btnAddSong);
        addSong.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText artistet = (EditText) findViewById(R.id.newartist);
        EditText songet = (EditText) findViewById(R.id.newsong);
        EditText yearet = (EditText) findViewById(R.id.newyear);

        String artist= artistet.getText().toString().trim();
        String song = songet.getText().toString().trim();
        String year = yearet.getText().toString().trim();

        if (artist.isEmpty() ) {
            popupAlert("artist must be set");
        } else if(song.isEmpty() ){
            popupAlert("song must be set");
        } else if(artist.isEmpty() ){
            popupAlert("year must be set");
        } else {
            PostSongTask t = new PostSongTask();
            t.execute(artist, song, year);
        }

    }

    private void popupAlert(String message){
        new AlertDialog.Builder(this).setPositiveButton("OK",null).setMessage(message).show();
    }

    class PostSongTask extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... input) {
            String artist = input[0];
            String song = input[1];
            String year = input[2];
            String postData = "artist=" + artist
                    + "&song=" + song
                    + "&year=" + year;

            System.out.println("AAA POST DATA:"+postData);

            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL);
                conn = (HttpURLConnection) url.openConnection();

                // For POST
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(postData.length());

                OutputStream out = null;
                out = conn.getOutputStream();
                out.write(postData.getBytes());
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuffer result = new StringBuffer();
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                    return result.toString();
                } else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            } catch (IOException e) {
                return e.toString();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        public void onPostExecute(String result) {
            TextView resultText = (TextView) findViewById(R.id.postResult);
            resultText.setText(result);
        }
    }

}


