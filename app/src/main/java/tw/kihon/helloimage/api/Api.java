package tw.kihon.helloimage.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import tw.kihon.helloimage.BuildConfig;

/**
 * Created by kihon on 2017/06/07.
 */

public abstract class Api {

    public static final String API_BASE_URL = "https://pixabay.com/api/";

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public interface Pixabay {
        @GET("?key=5241194-8a3a35f267b5afa498d2faab4")
        Call<Response.SearchImages> searchImages(@QueryMap(encoded = true) Map<String, String> params);
    }

    public abstract static class RequestBody {

        public static class SearchImages extends RequestBody {
            @SerializedName("q")
            String query;

            public SearchImages setQuery(String query) {
                this.query = query;
                return this;
            }
        }

        public Map<String, String> toMap() {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Gson gson = new Gson();
            System.out.println(getInstance());
            String json = gson.toJson(getInstance());
            return gson.fromJson(json, type);
        }

        private synchronized RequestBody getInstance(){
            return this;
        }

    }

    public static class Response {

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }


        public static class SearchImages extends Response {

            /**
             * total : 4692
             * totalHits : 500
             * hits : [{"id":195893,"pageURL":"https://pixabay.com/en/blossom-bloom-flower-yellow-close-195893/","type":"photo","tags":"blossom, bloom, flower","previewURL":"https://static.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg","previewWidth":150,"previewHeight":84,"webformatURL":"https://pixabay.com/get/35bbf209db8dc9f2fa36746403097ae226b796b9e13e39d2_640.jpg","webformatWidth":640,"webformatHeight":360,"imageWidth":4000,"imageHeight":2250,"imageSize":4731420,"views":7671,"downloads":6439,"favorites":1,"likes":5,"comments":2,"user_id":48777,"user":"Josch13","userImageURL":"https://static.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg"}]
             */

            @SerializedName("total")
            public int total;
            @SerializedName("totalHits")
            public int totalHits;
            @SerializedName("hits")
            public List<Hits> hits;

            public static class Hits {
                /**
                 * id : 195893
                 * pageURL : https://pixabay.com/en/blossom-bloom-flower-yellow-close-195893/
                 * type : photo
                 * tags : blossom, bloom, flower
                 * previewURL : https://static.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg
                 * previewWidth : 150
                 * previewHeight : 84
                 * webformatURL : https://pixabay.com/get/35bbf209db8dc9f2fa36746403097ae226b796b9e13e39d2_640.jpg
                 * webformatWidth : 640
                 * webformatHeight : 360
                 * imageWidth : 4000
                 * imageHeight : 2250
                 * imageSize : 4731420
                 * views : 7671
                 * downloads : 6439
                 * favorites : 1
                 * likes : 5
                 * comments : 2
                 * user_id : 48777
                 * user : Josch13
                 * userImageURL : https://static.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg
                 */

                @SerializedName("id")
                public int id;
                @SerializedName("pageURL")
                public String pageURL;
                @SerializedName("type")
                public String type;
                @SerializedName("tags")
                public String tags;
                @SerializedName("previewURL")
                public String previewURL;
                @SerializedName("previewWidth")
                public int previewWidth;
                @SerializedName("previewHeight")
                public int previewHeight;
                @SerializedName("webformatURL")
                public String webformatURL;
                @SerializedName("webformatWidth")
                public int webformatWidth;
                @SerializedName("webformatHeight")
                public int webformatHeight;
                @SerializedName("imageWidth")
                public int imageWidth;
                @SerializedName("imageHeight")
                public int imageHeight;
                @SerializedName("imageSize")
                public int imageSize;
                @SerializedName("views")
                public int views;
                @SerializedName("downloads")
                public int downloads;
                @SerializedName("favorites")
                public int favorites;
                @SerializedName("likes")
                public int likes;
                @SerializedName("comments")
                public int comments;
                @SerializedName("user_id")
                public int userId;
                @SerializedName("user")
                public String user;
                @SerializedName("userImageURL")
                public String userImageURL;
            }
        }
    }

    public abstract static class Callback<T> implements retrofit2.Callback<T> {

        private static final String TAG = "Callback";
        private static final int LOGCAT_MAX_LENGTH = 3950;

        @Override
        public void onResponse(Call<T> call, retrofit2.Response<T> response) {
            if (!response.isSuccessful()) {
                try {
                    Log.e(TAG, response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (BuildConfig.DEBUG) {
                    try {
                        String jsonString = new JSONObject(response.body().toString()).toString(3);
                        while (jsonString.length() > LOGCAT_MAX_LENGTH) {
                            int substringIndex = jsonString.lastIndexOf(",", LOGCAT_MAX_LENGTH);
                            if (substringIndex == -1)
                                substringIndex = LOGCAT_MAX_LENGTH;
                            Log.v(TAG, jsonString.substring(0, substringIndex));
                            jsonString = jsonString.substring(substringIndex).trim();
                        }
                        Log.v(TAG, jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage(), t);
        }
    }



}
