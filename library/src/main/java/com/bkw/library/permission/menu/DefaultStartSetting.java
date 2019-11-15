package com.bkw.library.permission.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class DefaultStartSetting implements IMenu {
    @Override
    public Intent startActivity(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("packages", context.getPackageName(), null));
        return intent;
    }
}
