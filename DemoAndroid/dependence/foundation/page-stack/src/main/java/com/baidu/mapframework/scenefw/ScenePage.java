package com.baidu.mapframework.scenefw;

//import org.jetbrains.annotations.NotNull;

import com.baidu.mapframework.app.fpstack.BasePage;
import com.baidu.mapframework.app.pagestack.R;
import com.baidu.mapframework.common.util.ScreenUtils;
//import com.baidu.mapframework.common.config.GlobalConfig;
//import com.baidu.mapframework.voice.sdk.model.VoiceResult;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScenePage extends BasePage {

    public static final String PAGE_SCENE_KEY = "page_scene_nav_key";

    public static int sVoiceTopMarginDp = 130;

    private SceneDirector director = SceneDirector.getDirectorInstance();
    private View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SFLog.d("ScenePage: onCreateView");
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.route_root_basepage, null);
            final ViewGroup containerView = (ViewGroup) contentView.findViewById(R.id.route_scene_container);

            SceneContainer sceneContainer = new SceneContainer() {
                /*@NotNull*/
                @Override
                public ViewGroup getContainerView() {
                    return containerView;
                }

                @Override
                public void goBack() {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean( /*MapFramePage.BUNDLE_SHOW_ANIM*/ "show_anim", true);
                    ScenePage.this.goBack(bundle);
                }
            };
            director.setSceneContainer(sceneContainer);
        }
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SFLog.d("ScenePage: onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        if (isNavigateBack()) {
            Bundle params = getBackwardArguments();
            if (params != null && params.containsKey(PAGE_SCENE_KEY)) {
                // 从 page back后，跳转到指定场景
                String sceneId = params.getString(PAGE_SCENE_KEY);
                director.restoreToScene(sceneId, params);
            } else {
                // 从 page back后，恢复上一个场景
                director.restoreScene(params);
            }
        } else {
            // 首次进入页面，push 第一个场景
            Bundle params = getArguments();
            if (params != null && params.containsKey(PAGE_SCENE_KEY)) {
                String sceneId = params.getString(PAGE_SCENE_KEY);
                director.pushScene(sceneId, params);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        director.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        director.onPause();
    }

    @Override
    public void onDestroy() {
        SFLog.d("ScenePage: onDestroy");
        super.onDestroy();
        // Page 被销毁，执行 storeScene 逻辑，若无 currentScene，则直接退出
        director.storeScene();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        director.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onBackPressed() {
        // TODO Refactor v10.8.0
        // 从OpenApi进入的，back后直接退出
//        if (EntryController.fromThirdPart) {
//            EntryController.fromThirdPart = false;
//            BMEventBus.getInstance().post(new ExitEvent());
//            return true;
//        }
        SFLog.d("ScenePage: onBackPressed");
        // 分级处理后退逻辑
        return director.onBackPressed() // currentScene onBackPressed 处理
                || director.popScene() // 场景栈 back
                || super.onBackPressed(); // 页面栈 back
    }

    @Override
    public int voiceTopMargin() {
        return ScreenUtils.dip2px(sVoiceTopMarginDp);
    }

    @Override
    public String infoToUpload() {
        Scene scene = director.getCurrentScene();
        if (scene != null) {
            /*if (!GlobalConfig.getInstance().isVoiceNewTaskProgress()) {
                return scene.infoToUpload();
            }*/
        }
        return super.infoToUpload();

    }

   /* @Override
    public void handleVoiceResult(VoiceResult voiceResult) {
        Scene scene = director.getCurrentScene();
        if (scene != null) {
            scene.handleVoiceResult(voiceResult);
        }
        super.handleVoiceResult(voiceResult);
    }*/

    @Override
    public String getPageLogTag() {
        Scene scene = director.getCurrentScene();
        if (scene != null) {
            if (!TextUtils.isEmpty(scene.getTag())) {
                return scene.getTag();
            } else {
                return scene.getSceneLogTag();
            }
        }
        return "";
    }

    @Override
    public boolean supportFullScreen() {
        return true;
    }

    // 第一次从路线详情页进入AR步导时，权限请求的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (director != null && director.getCurrentScene() != null) {
            director.getCurrentScene().onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onGoBack() {
        super.onGoBack();
        director.clean();
    }
}
