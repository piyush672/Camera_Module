package com.cameramodule.ImagePicker

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresExtension
import com.cameramodule.ConstValues
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule

//The @ReactModule annotation is used to register the CameraModule class as a native module
// with the React Native framework.
//It's a way to tell React Native that this class is a native module that should be exposed to JavaScript code.
//The name parameter in the @ReactModule annotation specifies the name under which the module will be registered.
// In this case, it's set to "CameraModule", which matches the value returned by the getName() method.

@ReactModule(name = "CameraModule")
class ImagePickerModule internal constructor(context: ReactApplicationContext?) : ReactContextBaseJavaModule(context) {

    companion object{
        val MODULE_NAME = ConstValues.MODULE_NAME
    }
    lateinit var imageUri: Uri
    override fun getName(): String {
        return MODULE_NAME
    }

    private val request_code = ConstValues.CAMERA_REQUEST_CODE
    private val gallery_request_code= ConstValues.GALLERY_REQUEST_CODE
    lateinit var promise: Promise

    @ReactMethod
    fun onClickCamera(promise: Promise) {
        this.promise = promise
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Captured Image")
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        imageUri = reactApplicationContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        // Start the camera intent with the content URI
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        reactApplicationContext.currentActivity?.startActivityForResult(intent, request_code)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    @ReactMethod
    fun onPickFromGallery(promise: Promise){
        this.promise = promise
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        reactApplicationContext.currentActivity?.startActivityForResult(intent, gallery_request_code)
    }

    fun onImageCaptured(uriString: String) {
        promise.resolve(uriString) // Resolve the Promise with the URI
    }

}
