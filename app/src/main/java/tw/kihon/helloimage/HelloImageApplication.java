package tw.kihon.helloimage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import tw.kihon.helloimage.api.ApiRequest;

/**
 * Created by kihon on 2017/06/07.
 */

public class HelloImageApplication extends Application {

    private static final String TAG = "HelloImageApplication";

    private static HelloImageApplication sInstance;
    private Gson mGson;

    public synchronized static HelloImageApplication getInstance() {
        return sInstance;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        sInstance = this;
        GsonBuilder gsonBuilder = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting();
        mGson = gsonBuilder.create();
        super.onCreate();
        ApiRequest.initialize();

        Picasso picasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(24000))
                .build();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);
    }

    public Gson getGson() {
        return mGson;
    }
}
