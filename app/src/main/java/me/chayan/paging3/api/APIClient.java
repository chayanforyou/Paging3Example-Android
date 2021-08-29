package me.chayan.paging3.api;


import me.chayan.paging3.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class will provide us a single instance of Retrofit
 */
public class APIClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    // Define APIInterface
    private static APIRequests apiRequests;

    // Method to get APIInterface
    public static APIRequests getInstance() {

        // Check for null
        if (apiRequests == null) {

            // Optional - Setup Http logging for debug purpose
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);

            // Create OkHttp Client
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            // Set logging
            client.addInterceptor(interceptor);
            // Add request interceptor to add API key as query string parameter to each request
            client.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        // Add API Key as query string parameter
                        .addQueryParameter("api_key", BuildConfig.API_KEY)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            // Create retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    // set base url
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    // Add Gson converter
                    .addConverterFactory(GsonConverterFactory.create())
                    // Add RxJava support for Retrofit
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();

            // Init APIInterface
            apiRequests = retrofit.create(APIRequests.class);
        }
        return apiRequests;
    }
}
