package com.bkw.library.permission.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * vivo手机
 * @author bkw
 */
public class VIVOStartSettings implements IMenu {

    @Override
    public Intent startActivity(Context context) {
        //不同厂商不同设置
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.i1oo.secure");

        if (appIntent != null && Build.VERSION.SDK_INT < 23) {
            context.startActivity(appIntent);
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_SETTINGS);
        intent.setData(Uri.fromParts("packages", context.getPackageName(), null));
        return intent;
    }
}
