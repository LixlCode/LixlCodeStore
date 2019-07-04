package com.huawei.hicar;

import android.os.Bundle;
import android.widget.RemoteViews;

interface IRemoteCardService {

    int    registerCard(String packageName, in Bundle params);
    void   updateCard(int cardId, in RemoteViews remoteViews,  in Bundle params);
    void   removeCard(int cardId);

    void   setValue(int valueType,  in Bundle values);
    Bundle getValue(int valueType);
}
