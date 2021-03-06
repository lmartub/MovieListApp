package com.example.movielist.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movielist.Constant;
import com.example.movielist.Model.Movie;
import com.example.movielist.R;

import java.util.List;

public class MovieSmallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { //RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context myContext = null;

    public List<Movie> mItemList;

    public MovieSmallAdapter(List<Movie> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder) viewHolder).movieTitle.setText(mItemList.get(position).getOriginalTitle());
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

    private class ItemViewHolder extends RecyclerView.ViewHolder {
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

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    private void showLoadingView (LoadingViewHolder viewHolder, int position){
        // ProgressBar would be displayed
    }

    private void populateItemRows (ItemViewHolder viewHolder, int position){
        String itemImage = mItemList.get(position).getPosterPath();
        String itemTitle = mItemList.get(position).getTitle();
        String itemDescription = mItemList.get(position).getOverview();

        //viewHolder.movieImage.setImageURI();
        viewHolder.movieTitle.setText(itemTitle);
        viewHolder.movieDescription.setText(itemDescription);
    }

}
