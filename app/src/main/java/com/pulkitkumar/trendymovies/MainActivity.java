package com.pulkitkumar.trendymovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //Just declaring important things ;)

    JSONArray main;
    gridAdapter gridAdapter;
    GridView gridView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    //Start of Constants used throughout the file

    public static String TAG = "TrendyMovies";
    public static final String JSON_TAG = "JSON";
    public static final String IMG_TAG = "IMG";
    public static final String MOVIE_TAG = "MOV";

    public static String SORT_BY = "popular?";

    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String FULL_IMG_SIZE = "w500/";
    public static final String SMALL_IMG_SIZE = "w342/";

    public static final String POPULARITY = "popular?";
    public static final String VOTE_AVERAGE = "top_rated?";

    public static final String JSON_RESULTS_ARRAY = "results";
    public static final String JSON_POSTER_PATH = "poster_path";
    public static final String JSON_BACKDROP_PATH = "backdrop_path";
    public static final String JSON_MOVIE_TITLE = "title";
    public static final String JSON_MOVIE_RELEASE_DATE = "release_date";
    public static final String JSON_MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String JSON_MOVIE_OVERVIEW = "overview";
    //End of Constants

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.gridView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        gridAdapter = new gridAdapter(this);

        gridView.setAdapter(gridAdapter);
        update();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(MainActivity.this, movieDetails.class);
                    intent.putExtra(MainActivity.JSON_TAG, main.getJSONObject(i).toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });
    }


    public class fetchData extends AsyncTask<Void,Void,String> {
        String fullJson;


        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(
                        MainActivity.BASE_URL +
                        MainActivity.SORT_BY +
                        "&api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY
                );

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }

                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                if (buffer.length() == 0) {
                    return null;
                }

                fullJson = buffer.toString();
                Log.d("TrendyMovies",fullJson);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return fullJson;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(MainActivity.TAG, fullJson);
            try {
                JSONObject root = new JSONObject(fullJson);

                main = root.getJSONArray(MainActivity.JSON_RESULTS_ARRAY);

                for (int i = 0; i < main.length(); i++) {
                    JSONObject movieJSON = main.getJSONObject(i);
                    gridAdapter.add(movieJSON.getString(MainActivity.JSON_POSTER_PATH));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            gridAdapter.notifyDataSetChanged();
            gridView.smoothScrollToPosition(1);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_popularity) {
            MainActivity.SORT_BY = MainActivity.POPULARITY;
            update();
            return true;
        }
        else if (id == R.id.action_sort_rating){
            MainActivity.SORT_BY = MainActivity.VOTE_AVERAGE;
            update();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void update() {
        gridAdapter.clean();
        mSwipeRefreshLayout.setRefreshing(true);
        new fetchData().execute();
    }
}