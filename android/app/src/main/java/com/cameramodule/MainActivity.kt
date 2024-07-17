package com.cameramodule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.cameramodule.cameraModule.CameraModule
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import java.io.File

class MainActivity : ReactActivity() {

      /**
       * Returns the name of the main component registered from JavaScript. This is used to schedule
       * rendering of the component.
       */
      override fun getMainComponentName(): String = "CameraModule"
        val request_code=ConstValues.CAMERA_REQUEST_CODE_
      /**
       * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
       * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
       */
    override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)


    //For getting the image from camera
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_code && resultCode == Activity.RESULT_OK) {
            println("data is ${data?.extras?.get("data")}")
            val bitmap=data?.extras?.get("data") as Bitmap
            val uri = bitmap.toUri(applicationContext)// Get URI as String
            println("uri is $uri")
            // Get your CameraModule instance and pass the URI
            val reactContext = reactInstanceManager?.currentReactContext
            reactContext?.getNativeModule(CameraModule::class.java)?.onImageCaptured(uri.toString())
        }
    }
}

//extension function on bitmap to get uri
fun Bitmap.toUri(context: Context): Uri {
    val outputStream = context.contentResolver.openOutputStream(getTempUri(context))
    if (outputStream != null) {
        this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    }
    outputStream?.close()
    return getTempUri(context)
}

fun getTempUri(context: Context): Uri {
    return Uri.fromFile(File.createTempFile("temp", ".jpg", context.cacheDir))
}
