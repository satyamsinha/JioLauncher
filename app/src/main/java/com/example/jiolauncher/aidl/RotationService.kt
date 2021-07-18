package com.example.jiolauncher.aidl

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Toast

class RotationService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
    }

    override fun onBind(intent: Intent): IBinder? {
        // Return the interface
        return binder
    }

    private val binder: DeviceRotationAidlInterface.Stub = object : DeviceRotationAidlInterface.Stub() {


        @Throws(RemoteException::class)
        override fun onRotation(): Int {
            val dm = applicationContext.resources.displayMetrics // Screen rotation effected
            var x:Int
            if (dm.widthPixels == dm.heightPixels) {
                x=Configuration.ORIENTATION_SQUARE}
            else if (dm.widthPixels < dm.heightPixels)
                x=Configuration.ORIENTATION_PORTRAIT
            else{
                x=Configuration.ORIENTATION_LANDSCAPE
            }
            Toast.makeText(applicationContext,"x"+x,Toast.LENGTH_LONG).show()
            return x
        }

        @Throws(RemoteException::class)
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String
        ) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    companion object {
        private const val TAG = "RotationService"
        var rotationValue: Int = 0

    }

}