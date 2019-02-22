package com.trakt.tv.traktpoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<MovieInfo> mMovInfoList = new ArrayList<>();

        int value = getIntent().getExtras().getInt("position");

        MovieInfo movieInfo =  (MovieInfo) getIntent().getSerializableExtra("info");
//        MovieInfo movieInfo = mMovInfoList.get(value);

        TextView textViewtitle = (TextView) findViewById(R.id.textViewTitle);
        textViewtitle.setText(movieInfo.getTitle());
        TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
        textViewYear.setText(movieInfo.getYear()+",  Run Time: "+movieInfo.getRuntime()+" mins");
        TextView textViewOverview = (TextView) findViewById(R.id.textViewOverview);
        textViewOverview.setText(movieInfo.getOverview());

        String genreModified = movieInfo.getGenres().replaceAll("\\[", "")
        .replaceAll("\\\"", "")
        .replaceAll("]", "")
        .replaceAll(",", ", ");

        TextView textViewGenere = (TextView) findViewById(R.id.textViewGenere);
        textViewGenere.setText(genreModified);

        getSupportActionBar().setTitle(movieInfo.getTitle());
    }

}
