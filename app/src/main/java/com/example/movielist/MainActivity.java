package com.example.movielist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movielist.Model.Movie;
import com.example.movielist.adapter.MovieSmallAdapter;
import com.example.movielist.apirest.ServiceMovie;
import com.example.movielist.Model.MoviesResponse;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.movielist.apirest.ClientMovie.retrofit;

public class MainActivity extends AppCompatActivity {

    ModelActivity modelActivity;
    int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private RecyclerView recyclerView;
    private MovieSmallAdapter movieAdapter;
    private List<Movie> movieListAll;
    private Integer page = 1;

    boolean isLoading = false;
    private Integer positionScroll = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewList);

        modelActivity = new ModelActivity();

        initScrollListener();
    }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewList);

        movieListAll = new ArrayList<>();
        movieAdapter = new MovieSmallAdapter(movieListAll);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

        loadJSON();

    }

    private void loadJSON(){
        try {
            Cache cache = new Cache(getCacheDir(), cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache).addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if (!modelActivity.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                                request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
                            }
                            return chain.proceed(request);
                        }
                    }).build();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(Constant.URL_BASE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
            ServiceMovie service = retrofit.create(ServiceMovie.class);
            // For API to create options, Add api_key, language and page, the last two are optionals.
            Map<String, String> options = new HashMap<String, String>();
            options.put("api_key", Constant.API_KEY);
            options.put("language", "es-ES");
            options.put("page", String.valueOf(page));

            Call<MoviesResponse> repos = service.getPopularMovies(options);

            // Call Asynchronous to API
            repos.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, retrofit2.Response<MoviesResponse> response) {
                    List<Movie> movieList = response.body().getResults();
                    Collections.sort(movieList, Movie.BY_NAME_ALPHABETICAL);

                    // Add more movies
                    if (isLoading) {
                        movieListAll.remove(movieListAll.size() - 1);
                        movieAdapter.notifyItemRemoved(movieListAll.size());
                    }

                    for (int i = 0; i < movieList.size(); i++) {
                        movieListAll.add(movieList.get(i));
                    }
                    // Now load information in recyclerView...
                    if (!isLoading) {
                        movieAdapter = new MovieSmallAdapter(movieListAll);
                        recyclerView.setAdapter(movieAdapter);
                    }

                    recyclerView.smoothScrollToPosition(movieListAll.size()-20);
                    movieAdapter.notifyDataSetChanged();
                    isLoading = false;
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    // Show an alert with a error.
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() == movieListAll.size()-1) {
                        // bottom of list!
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        page++;

        movieListAll.add(null);
        movieAdapter.notifyItemInserted(movieListAll.size()-1);
        loadJSON();
    }


}