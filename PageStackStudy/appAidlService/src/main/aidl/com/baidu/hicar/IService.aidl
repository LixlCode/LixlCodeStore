// IService.aidl
package com.baidu.hicar;

// Declare any non-default types here with import statements

interface IService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    // void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
    //         double aDouble, String aString);


    // 自定义的aidl接口
    void callRemoteMethod();
}
