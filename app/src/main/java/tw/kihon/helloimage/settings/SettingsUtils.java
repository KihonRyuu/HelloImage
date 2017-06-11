package tw.kihon.helloimage.settings;

import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import tw.kihon.helloimage.HelloImageApplication;

/**
 * Created by kihon on 2017/06/11.
 */

public class SettingsUtils {

    private static final String TAG = "SettingsUtils";

    private static final String PREF_SEARCH_SUGGESTION = "pref_search_suggestion";


    private static final SharedPreferences sSharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(HelloImageApplication.getInstance());


    public static void setSearchSuggestions(String[] values) {
        sSharedPreferences.edit().putString(PREF_SEARCH_SUGGESTION, new Gson().toJson(values)).apply();
    }

    public static String[] getSearchSuggestions() {
        String suggestions = sSharedPreferences.getString(PREF_SEARCH_SUGGESTION, null);
        Log.d(TAG, "getSearchSuggestions: " + suggestions);
        return suggestions == null ? new String[]{} : new Gson().fromJson(suggestions, String[].class);
    }

}
