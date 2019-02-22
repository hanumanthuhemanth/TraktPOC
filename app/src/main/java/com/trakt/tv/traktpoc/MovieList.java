package com.trakt.tv.traktpoc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MovieList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , CustomAdapter.ItemClickListener {
    int responseCode = 0;
    ArrayList<MovieInfo> mMovInfoList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private CustomAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        adapter = new CustomAdapter();

        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setClickListener(this);

        new TestAsync().execute();
    }


    protected ArrayList<MovieInfo> getMoviesList() {
        try {

            String url = "https://api-v2launch.trakt.tv/movies/popular?page=1&extended=full,images";
            String CLIENT_ID = "ad005b8c117cdeee58a1bdb7089ea31386cd489b21e14b19818c91511f12a086";
            URL obj = new URL(url);
            final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("trakt-api-version", "2");
            con.setRequestProperty("trakt-api-key", CLIENT_ID);
            con.setRequestMethod("GET");
            con.setReadTimeout(10000);
            con.setConnectTimeout(10000);

            responseCode = 0;

            try {
                InputStream in = new BufferedInputStream(con.getInputStream());

                mMovInfoList.clear();
                mMovInfoList = readStream(in);

            } finally {
                con.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<MovieInfo> readStream(InputStream is) {
        ArrayList<MovieInfo> movieList = new ArrayList<MovieInfo>();
        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String jsonText = readAll(bufferedReader);

            List<String> allMovies = new ArrayList<String>();
            String line = null;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    allMovies.add(line);
                }
            } catch (IOException e) {
                Log.e("e", "Exception1 : " + e);
            }
            JsonParser parser = new JsonParser();
            JsonArray json = (JsonArray) parser.parse(jsonText);

            if (json != null) {
                for (int p = 0; p < json.size(); p++) {
                    JsonObject jsonObject = (JsonObject) json.get(p);
                    MovieInfo movie = new MovieInfo();
                    try {
                        if (jsonObject.has("title"))
                            movie.setTitle(jsonObject.get("title").getAsString());
                        if (jsonObject.has("year"))
                            movie.setYear(jsonObject.get("year").getAsInt());
                        if (jsonObject.has("tagline"))
                            movie.setTagline(jsonObject.get("tagline").getAsString());
                        if (jsonObject.has("overview"))
                            movie.setOverview(jsonObject.get("overview").getAsString());
                        if (jsonObject.has("runtime"))
                            movie.setRuntime(jsonObject.get("runtime").getAsInt());
                        if (jsonObject.has("trailer"))
                            movie.setTrailer(jsonObject.get("trailer").getAsString());
                        if (jsonObject.has("homepage"))
                            movie.setHomepage(jsonObject.get("homepage").getAsString());
                        if (jsonObject.has("rating"))
                            movie.setRating(jsonObject.get("rating").getAsDouble());
                        /*if (jsonObject.has("images")) {
                            JsonObject jj = jsonObject.get("images").getAsJsonObject().get("poster").getAsJsonObject();
                            if (jj.has("thumb")) {
//                                movie.setBanner(jj.get("thumb")!=null?jj.get("thumb").getAsString():"");
                                Log.d("IMAGE", "" + jj.get("thumb").toString() + " ");
//                                jj.get("thumb") != null ? jj.get("thumb").getAsByte()  : "");
                            }
                        }*/
                        if (jsonObject.has("genres")) {
                            JsonArray jj = jsonObject.get("genres").getAsJsonArray();
                            movie.setGenres(jj.toString());
                        }

                        if (jsonObject.has("ids")) {
                            JsonObject joID = jsonObject.getAsJsonObject("ids");
                            if (joID.has("imdb"))
                                movie.setId_IMDB(joID.get("imdb").getAsString());
                            if (joID.has("tmdb"))
                                movie.setId_TMDB(joID.get("tmdb").getAsString());
                            if (joID.has("trakt"))
                                movie.setTrakt(joID.get("trakt").getAsInt());
                            if (joID.has("slug"))
                                movie.setSlug(joID.get("slug").getAsString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    movieList.add(movie);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void onRefresh() {
        new TestAsync().execute();
    }

    @Override
    public void onItemClick(View view, int position) {
        MovieInfo movieInfo=mMovInfoList.get(position);
        Intent i= new Intent(MovieList.this,MovieDetails.class);
        i.putExtra("position", position);
        i.putExtra("info", movieInfo);
        startActivity(i);
    }

    class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        protected String doInBackground(Void... arg0) {
            ArrayList<MovieInfo> response = getMoviesList();
            return null;
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);
            adapter.addItems(mMovInfoList);
        }
    }

}
