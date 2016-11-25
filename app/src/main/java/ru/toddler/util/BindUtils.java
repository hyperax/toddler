package ru.toddler.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.text.DateFormat;
import java.util.Date;

public class BindUtils {

    @NonNull
    private static Context getAppContext(View view) {
        return view.getContext().getApplicationContext();
    }

    @BindingAdapter({"imageUrl", "imageError"})
    public static void loadImage(ImageView view, String imageUrl, Drawable imageError) {
        Glide.with(getAppContext(view))
                .load(imageUrl)
                .centerCrop()
                .crossFade()
                .error(imageError)
                .into(view);
    }

    @BindingAdapter(value = {"imageUrl", "progressView", "placeholder"}, requireAll = false)
    public static void loadImageWithProgressBar(ImageView view,
                                                @Nullable String imageUrl,
                                                @Nullable View progressView,
                                                @Nullable Drawable placeholder) {
        if (NpeUtils.isEmpty(imageUrl)) {
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            setImageViewPlaceholder(view, placeholder);
        } else {
            Glide.with(getAppContext(view))
                    .load(imageUrl)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ViewUtils.setVisible(progressView, false);
                            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            view.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            ViewUtils.setVisible(progressView, false);
                            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            setImageViewPlaceholder(view, placeholder);
                        }

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            ViewUtils.setVisible(progressView, true);
                        }
                    });
        }
    }

    private static void setImageViewPlaceholder(@Nullable ImageView imageView, @Nullable Drawable placeholder) {
        if (imageView != null && placeholder != null) {
            imageView.setImageDrawable(placeholder);
        }
    }

    @BindingAdapter({"dateSeconds"})
    public static void formatDate(TextView textView, long seconds) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        textView.setText(dateFormat.format(new Date(seconds * 1000)));
    }

    @BindingAdapter({"linkRef", "linkText"})
    public static void formatHtml(TextView textView, @Nullable String ref, @Nullable String text) {
        if (NpeUtils.isEmpty(ref)) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            String rawStr = String.format("<a href=\"%1$s\">%2$s</a>", ref, text);
            textView.setText(CompatUtils.fromHtml(rawStr));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setVisibility(View.VISIBLE);
        }
    }
}
