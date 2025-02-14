package ai.elimu.content_provider.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Helps detect upgrades from previously installed versions of the app.
 */
public class VersionHelper {

    public static int getAppVersionCode(Context context) {
        Log.i(VersionHelper.class.getName(), "getAppVersionCode");

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Stores the version code of the application currently installed. And detects upgrades from previously installed
     * versions.
     */
    public static void updateAppVersion(Context context) {
        Log.i(VersionHelper.class.getName(), "updateAppVersion");

        // Check if the application's versionCode was upgraded
        int oldVersionCode = SharedPreferencesHelper.getAppVersionCode(context);
        int newVersionCode = VersionHelper.getAppVersionCode(context);
        if (oldVersionCode == 0) {
            SharedPreferencesHelper.storeAppVersionCode(context, newVersionCode);
            oldVersionCode = newVersionCode;
        }
        Log.i(VersionHelper.class.getName(),"oldVersionCode: " + oldVersionCode);
        Log.i(VersionHelper.class.getName(),"newVersionCode: " + newVersionCode);

        // Handle upgrade from previous version
        if (oldVersionCode < newVersionCode) {
            Log.i(VersionHelper.class.getName(), "Upgrading application from version " + oldVersionCode + " to " + newVersionCode + "...");

//            if (oldVersionCode < ???) {
//                ...
//            }

            SharedPreferencesHelper.storeAppVersionCode(context, newVersionCode);
        }
    }
}
