package me.chayan.paging3.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import me.chayan.paging3.model.Movie;

/**
 * Comparator for comparing Movie object to avoid duplicates
 */
public class MovieComparator extends DiffUtil.ItemCallback<Movie> {
    @Override
    public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return oldItem.getId().equals(newItem.getId());
    }
}
