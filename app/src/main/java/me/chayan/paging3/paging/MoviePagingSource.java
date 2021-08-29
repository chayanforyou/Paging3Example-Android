package me.chayan.paging3.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.chayan.paging3.api.APIClient;
import me.chayan.paging3.model.Movie;
import me.chayan.paging3.model.MovieResponse;

public class MoviePagingSource extends RxPagingSource<Integer, Movie> {

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Movie> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {

        // If page number is already there then init page variable with it otherwise we are loading fist page
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
        // Send request to server with page number
        return APIClient.getInstance()
                .getMoviesByPage(page)
                // Subscribe the result
                .subscribeOn(Schedulers.io())
                // Map result top List of movies
                .map(MovieResponse::getResults)
                // Map result to LoadResult Object
                .map(movies -> toLoadResult(movies, page))
                // when error is there return error
                .onErrorReturn(LoadResult.Error::new);
    }

    // Method to map Movies to LoadResult object
    private LoadResult<Integer, Movie> toLoadResult(List<Movie> movies, int page) {
        return new LoadResult.Page<>(movies, page == 1 ? null : page - 1, page + 1);
    }
}
