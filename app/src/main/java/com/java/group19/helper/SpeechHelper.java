package com.java.group19.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.java.group19.R;
import com.java.group19.listener.OnFinishSpeakingListener;

/**
 * Created by strongoier on 17/9/9.
 */

public class SpeechHelper {
    private static String TAG = "SpeechHelper";

    private static SpeechHelper mSpeechHelper;

    private boolean isSpeaking = false;
    private OnFinishSpeakingListener mListener;

    private Context mContext;

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    private SharedPreferences mSharedPreferences;

    public static SpeechHelper getInstance(Context context) {
        synchronized (SpeechHelper.class) {
            if (mSpeechHelper == null) {
                mSpeechHelper = new SpeechHelper(context);
            }
        }
        return mSpeechHelper;
    }

    private SpeechHelper(Context context) {
        // 初始化合成对象
        mContext = context;
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        mSharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.iflytek_prefer_name), Context.MODE_PRIVATE);
    }

    @Override
    protected void finalize() throws Throwable {
        if (null != mTts) {
            mTts.stopSpeaking();
            mTts.destroy();
        }
        super.finalize();
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setOnFinishSpeakingListener(OnFinishSpeakingListener listener) {
        mListener = listener;
    }

    public void speak(String text) {
        if (null == mTts) {
            // 创建单例失败，与21001错误为同样原因，参考http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            showTip("创建对象失败，请确认libmsc.so放置正确，且有调用createUtility进行初始化");
            return;
        }
        // 开始合成
        // 收到onCompleted回调时，合成结束、生成合成音频
        // 合成的音频格式：只支持pcm格式
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            showTip("语音合成失败,错误码: " + code);
        }
    }

    public void stop() {
        // 取消合成
        mTts.stopSpeaking();
        isSpeaking = false;
        mListener.onFinishSpeaking();
    }

    public void pause() {
        // 暂停播放
        mTts.pauseSpeaking();
    }

    public void resume() {
        // 继续播放
        mTts.resumeSpeaking();
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            isSpeaking = true;
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(mContext.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(mContext.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                isSpeaking = false;
                mListener.onFinishSpeaking();
                showTip("播放完成");
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    private void showTip(final String str) {
        Log.i(TAG, "showTip: " + str);
    }

    /**
     * 参数设置
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        // 设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        // 设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        // 设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, mContext.getFilesDir().toString() + "/msc/tts.wav");
    }
}
