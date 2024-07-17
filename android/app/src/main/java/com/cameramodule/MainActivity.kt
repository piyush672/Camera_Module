package com.cameramodule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.cameramodule.ImagePicker.ImagePickerModule
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import java.io.File
import java.io.FileOutputStream

class MainActivity : ReactActivity() {

      /**
       * Returns the name of the main component registered from JavaScript. This is used to schedule
       * rendering of the component.
       */
      override fun getMainComponentName(): String = "CameraModule"
        val request_code=ConstValues.CAMERA_REQUEST_CODE
        val gallery_request_code=ConstValues.GALLERY_REQUEST_CODE
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

            val reactContext = reactInstanceManager?.currentReactContext
            val cameraModule = reactContext?.getNativeModule(ImagePickerModule::class.java) as? ImagePickerModule
            cameraModule?.let {
                it.onImageCaptured(it.imageUri.toString())
            }

        }
        else if(requestCode==gallery_request_code && resultCode==Activity.RESULT_OK){
            val dataGallery = data?.data as Uri
            val uri = uriToTempImage(dataGallery,applicationContext)
            val reactContext = reactInstanceManager?.currentReactContext
            reactContext?.getNativeModule(ImagePickerModule::class.java)?.onImageCaptured("file://${uri?.absolutePath}")
        }
    }
}

fun uriToTempImage(uri: Uri, context: Context): File? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val tempFile = File.createTempFile("temp_image", ".jpg")
    val outputStream = FileOutputStream(tempFile)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
    return tempFile
}
