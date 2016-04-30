package com.pulkitkumar.trendymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class movieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView backdrop = (ImageView) findViewById(R.id.backdropView);
        ImageView poster = (ImageView) findViewById(R.id.posterView);
        TextView titleView = (TextView) findViewById(R.id.titleView);
        TextView releasedView = (TextView) findViewById(R.id.releasedView);
        TextView starText = (TextView) findViewById(R.id.starText);
        TextView overviewText = (TextView) findViewById(R.id.overviewView);

        try {
            JSONObject movieJSON;

            movieJSON = new JSONObject(getIntent().getStringExtra(MainActivity.JSON_TAG));

            final String backdropURI = movieJSON.getString(MainActivity.JSON_BACKDROP_PATH);
            final String posterURI = movieJSON.getString(MainActivity.JSON_POSTER_PATH);

            Picasso.with(this)
                    .load(MainActivity.IMG_BASE_URL + MainActivity.FULL_IMG_SIZE + backdropURI)
                    .fit()
                    .into(backdrop);

            Picasso.with(this)
                    .load(MainActivity.IMG_BASE_URL + MainActivity.FULL_IMG_SIZE + posterURI)
                    .fit()
                    .into(poster);

            final Intent intent = new Intent(movieDetails.this, fullImageActivity.class);
            intent.putExtra(MainActivity.MOVIE_TAG,movieJSON.getString(MainActivity.JSON_MOVIE_TITLE));

            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra(MainActivity.IMG_TAG, MainActivity.IMG_BASE_URL + MainActivity.FULL_IMG_SIZE + posterURI);
                    startActivity(intent);
                }
            });

            backdrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra(MainActivity.IMG_TAG, MainActivity.IMG_BASE_URL + MainActivity.FULL_IMG_SIZE + backdropURI);
                    startActivity(intent);
                }
            });

            titleView.setText(movieJSON.getString(MainActivity.JSON_MOVIE_TITLE));
            releasedView.setText((movieJSON.getString(MainActivity.JSON_MOVIE_RELEASE_DATE)).substring(0, 4));
            starText.setText(movieJSON.getString(MainActivity.JSON_MOVIE_VOTE_AVERAGE));
            overviewText.setText(movieJSON.getString(MainActivity.JSON_MOVIE_OVERVIEW));
            getSupportActionBar().setTitle(movieJSON.getString(MainActivity.JSON_MOVIE_TITLE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}