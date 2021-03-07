package com.example.movielist.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movielist.Constant;
import com.example.movielist.DetailMovieActivity;
import com.example.movielist.Model.Movie;
import com.example.movielist.R;

import org.w3c.dom.Text;

import java.util.List;

public class MovieSmallAdapter extends RecyclerView.Adapter<MovieSmallAdapter.MovieSmallHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context myContext = null;

    public List<Movie> mItemList;

    public MovieSmallAdapter(List<Movie> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public MovieSmallAdapter.MovieSmallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        myContext = parent.getContext();

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
            return  new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSmallAdapter.MovieSmallHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder) viewHolder).movieTitle.setText(mItemList.get(position).getTitle());
            ((ItemViewHolder) viewHolder).movieDescription.setText(mItemList.get(position).getOverview());
            String poster = Constant.URL_IMAGE_LIST + mItemList.get(position).getPosterPath();

            // Load Image in recyclerView ...
            ((ItemViewHolder) viewHolder).movieImage.setImageURI(Uri.parse(poster));
            Glide.with(myContext)
                    .load(poster)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(((ItemViewHolder) viewHolder).movieImage);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends MovieSmallAdapter.MovieSmallHolder {
        ImageView movieImage;
        TextView movieTitle;
        TextView movieDescription;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.itemMovieImage);
            movieTitle = itemView.findViewById(R.id.itemMovieTitle);
            movieDescription = itemView.findViewById(R.id.itemMovieDescription);
        }
    }

    private class LoadingViewHolder extends MovieSmallAdapter.MovieSmallHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public class MovieSmallHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView image;

        public MovieSmallHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.itemMovieTitle);
            description = (TextView)view.findViewById(R.id.itemMovieDescription);
            image=(ImageView)view.findViewById(R.id.itemMovieImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Movie clickedDataItem = mItemList.get(pos);
                        Intent intent = new Intent(myContext, DetailMovieActivity.class);
                        intent.putExtra("movies", clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
