package com.farmart.pro.Permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.cameramodule.ConstValues
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.util.Objects

class CustomPermissionModule internal constructor(context: ReactApplicationContext?) :
    ReactContextBaseJavaModule(context) {
    override fun getName(): String {
        return moduleName
    }

    @ReactMethod
    fun requestStoragePermission(promise: Promise) {
        val AndroidPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        val permission =
            ActivityCompat.checkSelfPermission(reactApplicationContext, AndroidPermission)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Objects.requireNonNull(
                currentActivity
            ).let {
                if (it != null) {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(AndroidPermission), 1
                    )
                }
            }
            promise.resolve(false) // Permission is not granted at this moment
        } else {
            promise.resolve(true) // Permission is already granted
        }
    }

    companion object {
        const val moduleName: String = ConstValues.CUSTOM_GALLERY_PERMISSION
    }
}