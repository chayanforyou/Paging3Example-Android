package me.chayan.paging3.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import me.chayan.paging3.databinding.MovieRowBinding;
import me.chayan.paging3.model.Movie;

/**
 * This adapter will handle listing of movies in recyclerview
 */
public class MoviesAdapter extends PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder> {

    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int MOVIE_ITEM = 1;

    public MoviesAdapter(@NotNull DiffUtil.ItemCallback<Movie> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return MovieViewHolder
        return new MovieViewHolder(MovieRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Get current movie
        Movie currentMovie = getItem(position);
        // Check for null
        if (currentMovie != null) {
            // Set Image of Movie using Picasso Library
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + currentMovie.getPosterPath())
                    .fit()
                    .into(holder.binding.imageViewMovie);

            // Set rating of movie
            holder.binding.textViewRating.setText(String.valueOf(currentMovie.getVoteAverage()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        // set ViewType
        return position == getItemCount() ? MOVIE_ITEM : LOADING_ITEM;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        // Define movie_item layout view binding
        MovieRowBinding binding;

        public MovieViewHolder(@NonNull MovieRowBinding binding) {
            super(binding.getRoot());
            // init binding
            this.binding = binding;
        }
    }
}
