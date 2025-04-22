package ai.elimu.content_provider.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

/**
 * Helps detect upgrades from previously installed versions of the app.
 */
object VersionHelper {
    fun getAppVersionCode(context: Context): Int {
        Log.i(VersionHelper::class.java.name, "getAppVersionCode")

        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Could not get package name: $e")
        }
    }

    /**
     * Stores the version code of the application currently installed. And detects upgrades from previously installed
     * versions.
     */
    fun updateAppVersion(context: Context) {
        Log.i(VersionHelper::class.java.name, "updateAppVersion")

        // Check if the application's versionCode was upgraded
        var oldVersionCode = SharedPreferencesHelper.getAppVersionCode(context)
        val newVersionCode = getAppVersionCode(context)
        if (oldVersionCode == 0) {
            SharedPreferencesHelper.storeAppVersionCode(context, newVersionCode)
            oldVersionCode = newVersionCode
        }
        Log.i(VersionHelper::class.java.name, "oldVersionCode: $oldVersionCode")
        Log.i(VersionHelper::class.java.name, "newVersionCode: $newVersionCode")

        // Handle upgrade from previous version
        if (oldVersionCode < newVersionCode) {
            Log.i(
                VersionHelper::class.java.name,
                "Upgrading application from version $oldVersionCode to $newVersionCode..."
            )

            //            if (oldVersionCode < ???) {
//                ...
//            }
            SharedPreferencesHelper.storeAppVersionCode(context, newVersionCode)
        }
    }
}
