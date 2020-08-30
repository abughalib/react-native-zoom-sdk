package com.abugh.zoom_sdk_kotlin

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

public class ZoomPackage : ReactPackage{
    override fun createNativeModules(reactContext: ReactApplicationContext):
            MutableList<NativeModule> {

        return mutableListOf(ZoomModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext):
            MutableList<ViewManager<View, ReactShadowNode<*>>> {

        return mutableListOf()
    }

}