package com.baidu.mapframework.app.fpstack;

import java.io.Serializable;

/**
 * Created by wangqingfang on 2017/2/8.
 */
public interface VoiceCallbackListener extends Serializable {
    /**
     * 开始录音
     */
    void asrReady();

    /**
     * 检测到人声
     */
    void asrBegin();

    /**
     * 录音结束
     */
    void asrEnd();

    /**
     * 识别结束
     */
    void asrFinish();

    /**
     * 识别失败
     */
    void asrRepeat(boolean isrepeat);

    /**
     * 开始TTS
     */
    void ttsPlay();

    /**
     * 结束TTS
     */
    void ttsEnd(boolean isDuolun);

    /**
     * 结束动画
     */
    void ttsEndAniStart();
}
