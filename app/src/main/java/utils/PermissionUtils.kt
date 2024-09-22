package utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun isPermissionGranted(
    resultRequestCode: Int, requestCode: Int,
    grantResults: IntArray, function: (Boolean) -> Unit?
) {
    if (requestCode == resultRequestCode) {
        function(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
    }

}

fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
}