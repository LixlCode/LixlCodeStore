package com.baidu.mapframework.webview;

import android.content.Intent;
import android.os.Bundle;

/**
 * User: liuda
 * Date: 3/26/15
 * Time: 3:48 PM
 */
public interface IActivityResultListener {

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    boolean onBackFromOtherPage(Bundle args);
}
