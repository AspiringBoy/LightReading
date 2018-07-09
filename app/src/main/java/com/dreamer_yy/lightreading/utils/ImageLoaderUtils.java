package com.dreamer_yy.lightreading.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.dreamer_yy.lightreading.R;

/**
 * Created by Dreamer__YY on 2018/5/14.
 */

public class ImageLoaderUtils {

    /**
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, Object url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_launcher_background)//占位图
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))//原图和变换后的图都缓存
                .transition(new DrawableTransitionOptions().crossFade(800))//淡入淡出0.8秒
                .into(imageView);
    }

    /**
     *
     * @param context
     * @param url
     * @param imageView
     * @param requestOptions
     */
    public static void loadImage(Context context, Object url, ImageView imageView,RequestOptions requestOptions) {
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade(800))//淡入淡出0.8秒
                .into(imageView);
    }

    /**
     *
     * @param context
     * @param url
     * @param imageViewTarget
     */
    public static void loadImage(Context context, Object url, ImageViewTarget imageViewTarget) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))//原图和变换后的图都缓存
                .transition(new DrawableTransitionOptions().crossFade(800))//淡入淡出0.8秒
                .into(imageViewTarget);
    }

    /**
     *
     * @param context
     * @param url
     * @param imageView
     * @param requestListener
     */
    public static void loadImage(Context context, Object url, ImageView imageView, RequestListener requestListener) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))//原图和变换后的图都缓存
                .transition(new DrawableTransitionOptions().crossFade(800))//淡入淡出0.8秒
                .listener(requestListener)
                .into(imageView);
    }

    /**
     *
     * @param fragment
     * @param url
     * @param imageView
     * @param requestOptions
     */
    public static void loadImage(Fragment fragment, Object url, ImageView imageView,RequestOptions requestOptions) {
        Glide.with(fragment)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade(800))//淡入淡出0.8秒
                .into(imageView);
    }
}
