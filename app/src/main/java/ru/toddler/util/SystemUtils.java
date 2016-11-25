package ru.toddler.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.android.annotations.Nullable;
import com.annimon.stream.Stream;

public class SystemUtils {

    public static final String GOOGLE_PLAY_APP_URL = "market://details?id=ru.ireca.menu";

    private SystemUtils() {
    }

    /**
     * <p>
     * Gets a System property, defaulting to {@code null} if the property cannot be read.
     * </p>
     * <p>
     * If a {@code SecurityException} is caught, the return value is {@code null} and a message is written to
     * {@code System.err}.
     * </p>
     *
     * @param property the system property name
     * @return the system property value or {@code null} if a security problem occurs
     */
    public static String getSystemProperty(final String property) {
        try {
            return System.getProperty(property);
        } catch (final SecurityException ex) {
            // we are not allowed to look at this property
            System.err.println("Caught a SecurityException reading the system property '" + property
                    + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    public static void requestPermission(Activity activity, int requestId, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestId);
    }

    public static void requestPermission(Fragment fragment, int requestId, String... permissions) {
        fragment.requestPermissions(permissions, requestId);
    }

    public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isPermissionsGranted(@Nullable int[] grantResults) {
        return Stream.of(ConvertUtils.toList(grantResults))
                .allMatch(gr -> gr == PackageManager.PERMISSION_GRANTED);
    }

    public static Intent getGooglePlayIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(GOOGLE_PLAY_APP_URL));
        return intent;
    }
}
