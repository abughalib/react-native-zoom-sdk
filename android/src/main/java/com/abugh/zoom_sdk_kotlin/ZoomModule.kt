package com.abugh.zoom_sdk_kotlin

import android.util.Log
import com.facebook.react.bridge.*
import us.zoom.sdk.*

public class ZoomModule(reactContext: ReactApplicationContext?) :
    ReactContextBaseJavaModule(reactContext), ZoomSDKInitializeListener, MeetingServiceListener, LifecycleEventListener {

    private val TAG: String = "ZoomSDK"

    private lateinit var reactContext: ReactApplicationContext
    private var isInitialized: Boolean = false
    private lateinit var initializePromise: Promise
    private var meetingPromise: Promise? = null;

    init {
        this.reactContext = reactContext!!

        reactContext.addLifecycleEventListener(this)

    }

    override fun getName(): String {
        return "ZoomSDK"
    }

    @ReactMethod
    public fun initialize(sdkKey: String, sdkSecret: String, domain: String, promise: Promise) {
        val zoomSDK: ZoomSDK = ZoomSDK.getInstance();
        if(isInitialized) {
            promise.resolve("Already Initialized Zoom SDK Successfully")
            return
        }
        isInitialized = zoomSDK.isInitialized;

        try {
            initializePromise = promise;

            if(reactContext.currentActivity != null){
                reactContext.currentActivity!!.runOnUiThread(Runnable {
                    run() {
                        val params: ZoomSDKInitParams = ZoomSDKInitParams()
                        params.appKey = sdkKey
                        params.appSecret = sdkSecret
                        params.domain = domain
                        //params.jwtToken = jwtToken //JWT Token
                        zoomSDK.initialize(reactContext.getCurrentActivity(), this, params)
                        Log.i("Initialization Result", isInitialized.toString())
                    }
                });
            }

        }catch (e: Exception){
            promise.reject("UNEXPECTED ERROR", e)
            return
        }
    }

    @ReactMethod
    public fun startMeeting(
            displayName: String,
            meetingNo: String,
            userId: String,
            userType: Int,
            zoomAccessToken: String,
            promise: Promise

    ){

        try{
            meetingPromise = promise

            val zoomSDK = ZoomSDK.getInstance()
            if(!zoomSDK.isInitialized){
                promise.reject("ERROR ZOOM START",
                        "ZoomSDK has not been initialized successfully")
                return
            }

            val meetingService: MeetingService = zoomSDK.meetingService
            if(meetingService.meetingStatus != MeetingStatus.MEETING_STATUS_IDLE){
                var lMeetingno: Long = 0
                try {
                    lMeetingno = meetingNo.toLong()
                }catch (e: NumberFormatException){
                    promise.reject("ERROR_ZOOM_START", "Invalid meeting number$meetingNo")
                    return
                }

                if (meetingService.currentRtcMeetingNumber == lMeetingno){
                    meetingService.returnToMeeting(reactContext.currentActivity)
                    promise.resolve("Already Joined Zoom Meeting")
                    return
                }
            }

            val opts = StartMeetingOptions()
            val params = StartMeetingParamsWithoutLogin();

            params.displayName = displayName;
            params.meetingNo = meetingNo
            params.userId = userId
            params.userType = userType
            params.zoomAccessToken = zoomAccessToken //ZoomToken

            val startMeetingResult = meetingService.startMeetingWithParams(
                    reactContext.currentActivity, params, opts)

            if(startMeetingResult != MeetingError.MEETING_ERROR_SUCCESS){
                promise.reject("ERROR_ZOOM_START", "startMeeting, errorCode=$startMeetingResult")
            }

        }catch (e: Exception){
            promise.reject("ZOOM_START", e)
        }
    }

    @ReactMethod
    public fun joinMeeting(
            displayName: String,
            meetingNo: String,
            promise: Promise
    ){

        try {

            meetingPromise = promise

            val zoomSDK: ZoomSDK = ZoomSDK.getInstance()

            if(!zoomSDK.isInitialized){
                promise.reject("ERROR_ZOOM_JOIN",
                        "ZoomSDK has not been initialized successfully")
                return
            }

            val meetingService: MeetingService = zoomSDK.meetingService

            val opts = JoinMeetingOptions()
            val params = JoinMeetingParams()

            params.displayName = displayName
            params.meetingNo = meetingNo

            val joinMeetingResult = meetingService.joinMeetingWithParams(
                    reactContext.currentActivity, params, opts
            )
            Log.i(TAG, "joinMeeting, joinMeetingResult=$joinMeetingResult")

            if (joinMeetingResult != MeetingError.MEETING_ERROR_SUCCESS){
                promise.reject("ERROR_ZOOM_JOIN", "joinMeeting, errorCode=$joinMeetingResult")
            }
        }catch (ex: java.lang.Exception){
            promise.reject("ERROR_UNEXPECTED_EXCEPTION", ex)
        }
    }

    @ReactMethod
    public fun joinMeetingWithPassword(
            displayName: String,
            meetingNo: String,
            password: String,
            promise: Promise
    ){

        try {
            meetingPromise = promise

            val zoomSDK: ZoomSDK = ZoomSDK.getInstance()

            if (!zoomSDK.isInitialized){
                promise.reject("ERROR_ZOOM_JOIN",
                        "ZoomSDK has not been initialized successfully")
                return
            }
            val meetingService: MeetingService = zoomSDK.meetingService
            val opts = JoinMeetingOptions()
            val params = JoinMeetingParams()

            params.displayName = displayName
            params.meetingNo = meetingNo
            params.password = password

            val joinMeetingResult = meetingService.joinMeetingWithParams(
                    reactContext.currentActivity, params, opts
            )
            if(joinMeetingResult != MeetingError.MEETING_ERROR_SUCCESS){
                promise.reject("ERR_ZOOM_JOIN",
                        "joinMeeting, errorCode=$joinMeetingResult")
            }

        }catch (ex: Exception){
            promise.reject("ERROR_UNEXPECTED_JOIN", ex)
        }
    }

    override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=$errorCode, internalErrorCode=$internalErrorCode")

        if(errorCode != ZoomError.ZOOM_ERROR_SUCCESS){
            initializePromise.reject(
                    "ERR_ZOOM_INITIALIZATION",
                    "Error: $errorCode, internalErrorCode=$internalErrorCode"
            )
        }else{
            registerListener()
            initializePromise.resolve("Initialize Zoom SDK successfully.")
        }

    }
    private fun registerListener(){
        Log.i(TAG, "registerListener")
        val zoomSDK = ZoomSDK.getInstance()
        val meetingService: MeetingService = zoomSDK.meetingService

        zoomSDK.meetingService?.addListener(this)

    }
    private fun unregisterListener(){
        Log.i(TAG, "unregisterListener")
        val zoomSDK: ZoomSDK = ZoomSDK.getInstance()

        if(zoomSDK.isInitialized){
            val meetingService: MeetingService = zoomSDK.meetingService
            meetingService.removeListener(this)
        }
    }

    override fun onCatalystInstanceDestroy() {
        unregisterListener()
    }

    override fun onZoomAuthIdentityExpired() {

    }

    override fun onMeetingStatusChanged(meetingStatus: MeetingStatus, errorCode: Int, internalErrorCode: Int) {
        Log.i(TAG, "onMeetingStatusChanged, meetingStatus=$meetingStatus, errorCode=$errorCode, internalErrorCode=$internalErrorCode")
        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {
            meetingPromise?.reject(
                    "ERR_ZOOM_MEETING",
                    "Error: $errorCode, internalErrorCode=$internalErrorCode"
            )
            meetingPromise = null
        } else if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING) {
            meetingPromise?.resolve("Connected to zoom meeting")
            meetingPromise = null
        }
    }

    override fun onHostDestroy() { unregisterListener() }

    override fun onHostResume() {}

    override fun onHostPause() {}


}