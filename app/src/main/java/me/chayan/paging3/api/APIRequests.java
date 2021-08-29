package me.chayan.paging3.api;

import io.reactivex.rxjava3.core.Single;
import me.chayan.paging3.model.MovieResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Define API request calls in this interface
 */
public interface APIRequests {

    // Define Get request with query string parameter as page number
    @GET("movie/popular")
    Single<MovieResponse> getMoviesByPage(@Query("page") int page);
}
