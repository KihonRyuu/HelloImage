package tw.kihon.helloimage.api;

import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Loggable;

import android.support.annotation.NonNull;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tw.kihon.helloimage.BuildConfig;
import tw.kihon.helloimage.HelloImageApplication;

import static tw.kihon.helloimage.api.Api.API_BASE_URL;

/**
 * Created by kihon on 2017/06/07.
 */

public class ApiRequest {

    private static final String TAG = "ApiRequest";

    private static ApiRequest sInstance;
    private Retrofit mRetrofit;
    private OkHttpClient mClient;

    public static ApiRequest getInstance() {
        if (sInstance == null) {
            sInstance = new ApiRequest();
        }
        return sInstance;
    }

    public static void initialize() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            clientBuilder
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .addInterceptor(new CurlInterceptor(new Loggable() {
                        @Override
                        public void log(String message) {
                            Log.v("Ok2Curl", message);
                        }
                    }));
        }

        OkHttpClient client = clientBuilder
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(HelloImageApplication.getInstance().getGson()))
                .client(client)
                .build();

        getInstance().setClient(client);
        getInstance().setRetrofit(retrofit);
    }

    public static void searchImages(Api.RequestBody.SearchImages params,
                                    Callback<Api.Response.SearchImages> callback) {
        checkRetrofitInitialized();
        checkRequestBodyNotEmpty(params);
        Api.Pixabay pixabay = getInstance().mRetrofit.create(Api.Pixabay.class);
        Call<Api.Response.SearchImages> searchImagesCall = pixabay.searchImages(params.toMap());
        searchImagesCall.enqueue(callback);
    }

    @NonNull
    private static <T extends Api.Response> Api.Callback<T> getDefaultCallback(Class<T> clazz) {
        return new Api.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                super.onResponse(call, response);
                Log.d(TAG, "onResponse: " + response.body().toString());
            }
        };
    }

    private static void checkRetrofitInitialized() {
        if (getInstance().mRetrofit == null) {
            throw new IllegalStateException("Retrofit must be initialized by calling ApiRequest.initialize().");
        }
    }

    private static void checkRequestBodyNotEmpty(Api.RequestBody data) {
        if (data == null) {
            throw new NullPointerException("RequestBody cannot be null.");
        }
    }

    private void setRetrofit(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    private void setClient(OkHttpClient client) {
        mClient = client;
    }

}
