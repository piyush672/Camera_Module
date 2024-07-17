package com.cameramodule.ImagePicker

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

class ImagePickerPackage : ReactPackage {
    override fun createNativeModules(context: ReactApplicationContext): MutableList<NativeModule> {
        return listOf(ImagePickerModule(context)).toMutableList()
    }

    override fun createViewManagers(context: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return mutableListOf()
    }
}