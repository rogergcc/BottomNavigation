package info.androidhive.bottomnavigation.utils;

import android.os.Build;

public final class AndroidUtils {
    public static boolean isLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
