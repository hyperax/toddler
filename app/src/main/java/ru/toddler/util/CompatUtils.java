package ru.toddler.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

public class CompatUtils {

    private CompatUtils() {}

    public static Spanned fromHtml(String text) {
        String nonNullText = NpeUtils.getNonNull(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(nonNullText, Html.FROM_HTML_MODE_COMPACT); // for 24 api and more
        }
        //noinspection deprecation
        return Html.fromHtml(nonNullText); // or for older api
    }

    public static void finishApplication(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.finishAffinity();
        } else {
            activity.finish();
            System.exit(0);
        }
    }
}
