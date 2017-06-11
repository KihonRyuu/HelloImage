package tw.kihon.helloimage.util;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.os.BuildCompat;
import android.util.DisplayMetrics;

/**
 * Created by kihon on 2017/06/07.
 */

public class Utils {

    public static int displayWidth;
    public static int displayHeight;

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static void getDefaultDisplay(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        displayWidth = metric.widthPixels;
        displayHeight = metric.heightPixels;
    }

    /**
     * @return {@code true} if the device is {@link Build.VERSION_CODES#LOLLIPOP} or later
     */
    public static boolean isLOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * @return {@code true} if the device is {@link Build.VERSION_CODES#N} or later
     */
    public static boolean isNOrLater() {
        return BuildCompat.isAtLeastN();
    }

}
