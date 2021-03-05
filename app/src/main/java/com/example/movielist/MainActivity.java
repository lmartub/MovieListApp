package com.example.movielist;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movielist.Model.Movie;
import com.example.movielist.apirest.ServiceMovie;
import com.example.movielist.Model.MoviesResponse;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import java.io.IOException;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelActivity = new ModelActivity();

        loadJSON();
    }

    private void loadJSON(){
        Cache cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache).addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!modelActivity.isNetworkAvailable((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE))) {
                            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
                        }
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Constant.URL_BASE).client(okHttpClient).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        ServiceMovie service = retrofit.create(ServiceMovie.class);
        Log.i("Info", "Llamar al servicio...");
        // For API to create options, Add api_key, language and page, the last two are optionals.
        Map<String, String> options = new HashMap<String, String>();
        options.put("api_key", Constant.API_KEY);
        options.put("language", "es-ES");
        options.put("page", "2");

        Call<MoviesResponse> repos = service.getPopularMovies(options);

        // Call Asynchronous to API
        repos.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, retrofit2.Response<MoviesResponse> response) {
                List<Movie> movieList = response.body().getResults();
                Collections.sort(movieList, Movie.BY_NAME_ALPHABETICAL);
                Log.i("INFO", String.valueOf(movieList.size() + " = film number"));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                // Show an alert with a error.
                Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("Info", "Fin de la ejecución del LoadJSON");

    }
}