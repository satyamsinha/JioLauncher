// DeviceRotationAidlInterface.aidl
package com.example.jiolauncher.aidl;

// Declare any non-default types here with import statements

interface DeviceRotationAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      int onRotation();

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}