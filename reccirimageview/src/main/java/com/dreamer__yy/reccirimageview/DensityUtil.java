package com.dreamer__yy.reccirimageview;

import android.content.Context;

/**
 * Created by Dreamer__YY on 2018/7/10.
 */

public class DensityUtil {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
