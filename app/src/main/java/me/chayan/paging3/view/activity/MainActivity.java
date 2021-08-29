package me.chayan.paging3.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import me.chayan.paging3.databinding.ActivityMainBinding;
import me.chayan.paging3.utils.GridSpace;
import me.chayan.paging3.utils.MovieComparator;
import me.chayan.paging3.view.adapter.MoviesAdapter;
import me.chayan.paging3.view.adapter.MoviesLoadStateAdapter;
import me.chayan.paging3.viewModel.MovieViewModel;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create View binding object
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create new MoviesAdapter object and provide
        MoviesAdapter moviesAdapter = new MoviesAdapter(new MovieComparator());

        // Create ViewModel
        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Subscribe to to paging data
        Disposable disposable = movieViewModel.pagingDataFlow.subscribe(moviePagingData -> {
            // submit new data to recyclerview adapter
            moviesAdapter.submitData(getLifecycle(), moviePagingData);
        });
        // Adding an Observable to the disposable
        compositeDisposable.add(disposable);

        // Create GridlayoutManger with span of count of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        // Finally set LayoutManger to recyclerview
        binding.recyclerViewMovies.setLayoutManager(gridLayoutManager);

        // Add ItemDecoration to add space between recyclerview items
        binding.recyclerViewMovies.addItemDecoration(new GridSpace(2, 12, true));

        // set adapter
        binding.recyclerViewMovies.setAdapter(
                // concat movies adapter with header and footer loading view
                // This will show end user a progress bar while pages are being requested from server
                moviesAdapter.withLoadStateFooter(
                        // Pass footer load state adapter.
                        // When we will scroll down and next page request will be sent
                        // while we get response form server Progress bar will show to end user
                        // If request success Progress bar will hide and next page of movies
                        // will be shown to end user or if request will fail error message and
                        // retry button will be shown to resend the request
                        new MoviesLoadStateAdapter(view -> moviesAdapter.retry())
                )
        );

        // set Grid span
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // If progress will be shown then span size will be 1 otherwise it will be 2
                return moviesAdapter.getItemViewType(position) == MoviesAdapter.LOADING_ITEM ? 1 : 2;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Using dispose will clear all and set isDisposed = true,
        // so it will not accept any new disposable
        compositeDisposable.dispose();
    }
}