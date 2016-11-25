package ru.toddler.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.BiFunction;
import com.jakewharton.rxbinding.view.RxView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

public class ViewUtils {

    public static final long THROTTLE_TIME_MILLS = 600;

    private ViewUtils() {
    }

    public static void setEnabledWithChild(ViewGroup parentView, boolean isEnabled) {
        parentView.setEnabled(isEnabled);
        int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parentView.getChildAt(i);
            if (child instanceof ViewGroup) {
                setEnabledWithChild((ViewGroup) child, isEnabled);
            } else {
                child.setEnabled(isEnabled);
            }
        }
    }

    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 21) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Context context) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }

        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View v = ((Activity) context).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void enableIcons(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableIcons(Menu menu) {
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (Exception e) {
                // the error is ignored
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Activity activity, int colorResId) {
        int resultColor;
        if (Build.VERSION.SDK_INT >= 23) {
            resultColor = activity.getResources().getColor(colorResId, activity.getTheme());
        } else {
            resultColor = activity.getResources().getColor(colorResId);
        }

        return resultColor;
    }

    public static void showOKDialog(Context context, @StringRes int title, @StringRes int message, boolean showCancel,
                                    DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener);
        if (showCancel){
            builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        }
        builder.show();
    }

    @NonNull
    public static <T> Optional<T> findRecurs(@NonNull View rootView, @NonNull Class<T> classType, @IdRes int viewId) {
        List<ViewGroup> searchList = new ArrayList<>();

        if (rootView instanceof ViewGroup) {
            searchList.add((ViewGroup) rootView);
        }

        while (searchList.size() != 0) {
            ViewGroup groupToCheck = searchList.get(0);
            int childCount = groupToCheck.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = groupToCheck.getChildAt(i);
                if (child.getId() == viewId && classType.isInstance(child)) {
                    return Optional.of(classType.cast(child));
                } else if (child instanceof ViewGroup) {
                    searchList.add((ViewGroup) child);
                }
            }
            searchList.remove(0);
        }

        return Optional.empty();
    }

    @NonNull
    public static Optional<View> findRecurs(@NonNull View rootView, @IdRes int viewId) {
        return findRecurs(rootView, View.class, viewId);
    }

    @NonNull
    public static <V> Optional<V> findView(@NonNull View rootView, Class<V> classType, @IdRes int viewId) {
        View view = rootView.findViewById(viewId);
        if (view != null && classType.isInstance(view)) {
            return Optional.of(classType.cast(view));
        }
        return Optional.empty();
    }

    @NonNull
    public static Optional<View> findView(@NonNull View rootView, @IdRes int viewId) {
        return findView(rootView, View.class, viewId);
    }

    @NonNull
    public static Optional<MenuItem> findItem(@NonNull Menu menu, @IdRes int itemId) {
        return Optional.ofNullable(menu.findItem(itemId));
    }

    @NonNull
    public static String getString(@NonNull Context context, @StringRes int str, @StringRes int... argRes) {
        if (!NpeUtils.isEmpty(argRes)) {
            int size = argRes.length;
            Object argString[] = new String[size];
            for (int i = 0; i < size; i++) {
                argString[i] = context.getString(argRes[i]);
            }
            return context.getString(str, argString);
        }
        return context.getString(str);
    }

    @NonNull
    public static String extractText(@Nullable TextView textView) {
        if (textView == null) {
            return NpeUtils.EMPTY_STRING;
        } else {
            return textView.getText().toString().trim();
        }
    }

    public static long extractLong(@Nullable TextView textView) {
        return ConvertUtils.parseLong(extractText(textView));
    }

    public static int extractInt(@Nullable TextView textView) {
        return ConvertUtils.parseInt(extractText(textView));
    }

    @NonNull
    public static BigDecimal extractBigDecimal(@Nullable TextView textView) {
        return MathUtils.getValue(extractText(textView));
    }

    /**
     * Method sets the {@code listener} on {@code view} and prevents multiple clicks.
     * By default the timeout between clicks is set to {@value ViewUtils#THROTTLE_TIME_MILLS} milliseconds.
     */
    public static void throttle(View view, View.OnClickListener listener) {
        if (view == null || listener == null) {
            return;
        }

        RxView.clicks(view)
                .throttleFirst(THROTTLE_TIME_MILLS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> listener.onClick(view));
    }

    @SafeVarargs
    private static <T> boolean restoreState(Bundle state, BiFunction<Bundle, T, Boolean> restoreOperator, T... items) {
        boolean isDataRestored = false;
        if (state != null && !NpeUtils.isEmpty(items)) {
            for (T item : items) {
                isDataRestored = restoreOperator.apply(state, item);
            }
        }

        return isDataRestored;
    }

    @SafeVarargs
    private static <T> void putState(Bundle state, BiConsumer<Bundle, T> putOperator, T... items) {
        if (state != null && NpeUtils.isEmpty(items)) {
            for (T item : items) {
                putOperator.accept(state, item);
            }
        }
    }

    private static BiFunction<Bundle, TextView, Boolean> textViewRestore = (state, textView) -> {
        String key = getKey(textView);
        if (state.containsKey(key)) {
            textView.setText(state.getString(key));
            return true;
        }
        return false;
    };

    public static boolean restoreState(Bundle state, TextView... textViews) {
        return restoreState(state, textViewRestore, textViews);
    }

    private static BiConsumer<Bundle, TextView> textViewPut = (state, textView) -> {
        String key = getKey(textView);
        state.putString(key, textView.getText().toString());
    };

    public static void putState(Bundle state, TextView... textViews) {
        putState(state, textViewPut, textViews);
    }

    private static BiFunction<Bundle, RatingBar, Boolean> ratingBarRestore = (state, ratingBar) -> {
        String key = getKey(ratingBar);
        if (state.containsKey(key)) {
            ratingBar.setRating(state.getFloat(key));
            return true;
        }
        return false;
    };

    public static boolean restoreState(Bundle state, RatingBar... bars) {
        return restoreState(state, ratingBarRestore, bars);
    }

    private static BiConsumer<Bundle, RatingBar> ratingBarPut = (state, ratingBar) -> {
        String key = getKey(ratingBar);
        state.putFloat(key, ratingBar.getRating());
    };

    public static void putState(Bundle state, RatingBar... bars) {
        putState(state, ratingBarPut, bars);
    }

    private static BiFunction<Bundle, CompoundButton, Boolean> checkBoxRestore = (state, checkBox) -> {
        String key = getKey(checkBox);
        if (state.containsKey(key)) {
            checkBox.setChecked(state.getBoolean(key));
            return true;
        }
        return false;
    };

    public static boolean restoreState(Bundle state, CompoundButton... checkBoxes) {
        return restoreState(state, checkBoxRestore, checkBoxes);
    }

    private static BiConsumer<Bundle, CompoundButton> checkBoxPut = (state, checkBox) -> {
        String key = getKey(checkBox);
        state.putBoolean(key, checkBox.isChecked());
    };

    public static void putState(Bundle state, CompoundButton... checkBoxes) {
        putState(state, checkBoxPut, checkBoxes);
    }

    private static <V extends View> String getKey(V view) {
        return view.getClass().getSimpleName() + view.getId();
    }

    public static <V extends View> boolean isContains(Bundle bundle, V view) {
        return bundle.containsKey(getKey(view));
    }

    /**
     * Builds {@link PopupMenu}
     *
     * @param context     Context the popup menu is running in, through which it
     *                    can access the current theme, resources, etc.
     * @param anchor      Anchor view for this popup. The popup will appear below
     *                    the anchor if there is room, or above it if there is not.
     * @param menu        Menu resource id for inflate menu from resources. Can be 0 for ignore inflating
     * @param enableIcons If flag {@code enableIcons} is true, then popup menu will have been built with icons
     */
    public static PopupMenu createPopupMenu(Context context, View anchor, @MenuRes int menu, boolean enableIcons) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);

        if (menu > 0) {
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(menu, popupMenu.getMenu());
        }

        if (enableIcons) {
            enableIcons(popupMenu);
        }

        return popupMenu;
    }

    /**
     * Sets visibility of view.
     *
     * @param view    View for setup visibility.
     * @param visible If flag is {@code true}, then visibility of view will be set VISIBLE,
     *                otherwise visibility will be set GONE
     *                <p>
     *                {@link View#setVisibility(int)}
     */
    public static void setVisible(View view, boolean visible) {
        NpeUtils.call(view, v -> {
            if (visible) {
                v.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Configures {@link TextView} for scrolling text
     * Notice that methods sets singleLine option for TextView and always selected text
     */
    public static void setupMarquee(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(-1); // from documentation set to -1 to repeat indefinitely.
        textView.setSingleLine();
        textView.setSelected(true);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
