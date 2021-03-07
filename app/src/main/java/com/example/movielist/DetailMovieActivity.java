package com.example.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.movielist.Model.Movie;
import com.example.movielist.adapter.MovieSmallAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class DetailMovieActivity extends AppCompatActivity {
    TextView description, userRating, releaseDate;
    ImageView imageView;

    Movie movie;
    String pictureMovie, movieName, synopsis, rating, dateOfRelease;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailmovie);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView)findViewById(R.id.movie_image_header);
        description = (TextView) findViewById(R.id.plotsynopsis);
        userRating = (TextView)findViewById(R.id.userrating);
        releaseDate = (TextView)findViewById(R.id.releasedate);

        Intent intentThatStarted = getIntent();
        if (intentThatStarted.hasExtra("movies")){
            movie = getIntent().getParcelableExtra("movies");

            pictureMovie = movie.getPosterPath();
            movieName = movie.getTitle();
            synopsis = movie.getOverview();
            rating = Double.toString(movie.getVoteAverage());

            // Change format date yyyy-mm-dd --> dd/mm/yyyy
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(movie.getReleaseDate());
                DateFormat formatted = new SimpleDateFormat("dd/MM/yyyy");
                dateOfRelease = formatted.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            dateOfRelease = movie.getReleaseDate();

            String poster = Constant.URL_IMAGE_LIST + pictureMovie;

            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

            description.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
            ((CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar)).setTitle(movieName);
        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }

    }
}
