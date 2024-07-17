package com.cameramodule.cameraModule

import android.content.Intent
import android.provider.MediaStore
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
class CameraModule internal constructor(context: ReactApplicationContext?) : ReactContextBaseJavaModule(context) {

    companion object{
        const val MODULE_NAME = "CameraModule"
    }
    override fun getName(): String {
        return MODULE_NAME
    }

    private val request_code = ConstValues.CAMERA_REQUEST_CODE_
    lateinit var promise: Promise

    @ReactMethod
    fun onClickCamera(promise: Promise) {
        this.promise = promise
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        reactApplicationContext.currentActivity?.startActivityForResult(intent, request_code)
    }

    fun onImageCaptured(uriString: String) {
        promise.resolve(uriString) // Resolve the Promise with the URI
    }

}
