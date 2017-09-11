package com.java.group19;

import com.baidu.tts.auth.AuthInfo;
import com.java.group19.listener.OnFinishSpeakingListener;

import android.content.Context;
import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.SynthesizerTool;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by strongoier on 17/9/9.
 */

public class TextSpeaker {

    private static TextSpeaker mTextSpeaker;
    private SpeechSynthesizer mSpeechSynthesizer;
    private Context mContext;
    private String mSampleDirPath;
    private boolean isSpeaking = false;
    private int count = 0;
    private OnFinishSpeakingListener listener;
    private static final int LENGTH_PER_SPEAK = 500;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    private static final String TAG = "TextSpeaker";

    public static TextSpeaker getInstance(Context context) {
        synchronized (TextSpeaker.class) {
            if (mTextSpeaker == null) {
                mTextSpeaker = new TextSpeaker(context);
            }
        }
        return mTextSpeaker;
    }

    public void setOnFinishSpeakingListener(OnFinishSpeakingListener listener) {
        this.listener = listener;
    }

    private TextSpeaker(Context context) {
        mContext = context;
        initialEnv();
        initialTts();
    }

    @Override
    protected void finalize() throws Throwable {
        mSpeechSynthesizer.release();
        mTextSpeaker = null;
        super.finalize();
    }

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String filesPath = mContext.getFilesDir().toString();
            toPrint("FilesPath=" + filesPath);
            mSampleDirPath = filesPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToFiles(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToFiles(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToFiles(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToFiles(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToFiles(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToFiles(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将需要的资源文件拷贝使用
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToFiles(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = mContext.getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initialTts() {
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(mContext);
        mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener() {
            /**
             * 合成开始，每句合成开始都会回调
             *
             * @param utteranceId
             */
            @Override
            public void onSynthesizeStart(String utteranceId) {
                if (++count > 0) {
                    isSpeaking = true;
                }
                toPrint("onSynthesizeStart utteranceId=" + utteranceId);
            }

            /**
             * 合成数据和进度的回调接口，分多次回调
             *
             * @param utteranceId
             * @param data 合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
             * @param progress 文本按字符划分的进度，比如：你好啊 进度是0-3
             */
            @Override
            public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
                toPrint("onSynthesizeDataArrived utteranceId=" + utteranceId + " progress=" + progress);
            }

            /**
             * 合成正常结束，每句合成正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
             *
             * @param utteranceId
             */
            @Override
            public void onSynthesizeFinish(String utteranceId) {
                toPrint("onSynthesizeFinish utteranceId=" + utteranceId);
            }

            /**
             * 播放开始，每句播放开始都会回调
             *
             * @param utteranceId
             */
            @Override
            public void onSpeechStart(String utteranceId) {
                toPrint("onSpeechStart utteranceId=" + utteranceId);
            }

            /**
             * 播放进度回调接口，分多次回调
             *
             * @param utteranceId
             * @param progress 文本按字符划分的进度，比如：你好啊 进度是0-3
             */
            @Override
            public void onSpeechProgressChanged(String utteranceId, int progress) {
                toPrint("onSpeechProgressChanged utteranceId=" + utteranceId + " progress=" + progress);
            }

            /**
             * 播放正常结束，每句播放正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
             *
             * @param utteranceId
             */
            @Override
            public void onSpeechFinish(String utteranceId) {
                if (--count == 0) {
                    isSpeaking = false;
                    try {
                        listener.onFinishSpeaking();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                toPrint("onSpeechFinish utteranceId=" + utteranceId);
            }

            /**
             * 当合成或者播放过程中出错时回调此接口
             *
             * @param utteranceId
             * @param error 包含错误码和错误信息
             */
            @Override
            public void onError(String utteranceId, SpeechError error) {
                toPrint("onError error=" + "(" + error.code + ")" + error.description + "--utteranceId=" + utteranceId);
            }
        });
        // 文本模型文件路径（离线引擎使用）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        // 声学模型文件路径（离线引擎使用）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID（离线授权）
        mSpeechSynthesizer.setAppId("10118286");
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey（在线授权）
        mSpeechSynthesizer.setApiKey("vlq6uv8TcLuN5BDfwtDP4SWe", "29f20147f4b899efadcf47e6f238958b");
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "3");
        // 设置音量
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置Mix模式的合成策略
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);

        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);

        if (authInfo.isSuccess()) {
            toPrint("auth success");
        } else {
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            toPrint("auth failed errorMsg=" + errorMsg);
        }

        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        int result = mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_SPEECH_MALE_MODEL_NAME);
        toPrint("loadEnglishModel result=" + result);

        //打印引擎信息和model基本信息
        printEngineInfo();
    }

    /**
     * 打印引擎so库版本号及基本信息和model文件的基本信息
     */
    private void printEngineInfo() {
        toPrint("EngineVersioin=" + SynthesizerTool.getEngineVersion());
        toPrint("EngineInfo=" + SynthesizerTool.getEngineInfo());
        String textModelInfo = SynthesizerTool.getModelInfo(mSampleDirPath + "/" + TEXT_MODEL_NAME);
        toPrint("textModelInfo=" + textModelInfo);
        String speechModelInfo = SynthesizerTool.getModelInfo(mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        toPrint("speechModelInfo=" + speechModelInfo);
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void speak(String text) {
        String[] texts = text.split("\n");
        for (String single : texts) {
            speakSingle(single);
        }
    }

    public void speakSingle(String text) {
        //mSpeechSynthesizer.speak文本的长度不能超过1024个GBK字节。
        while (text.length() > LENGTH_PER_SPEAK) {
            int result = mSpeechSynthesizer.speak(text.substring(0, LENGTH_PER_SPEAK));
            if (result < 0) {
                toPrint("error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122");
            }
            text = text.substring(LENGTH_PER_SPEAK);
        }
        int result = mSpeechSynthesizer.speak(text);
        if (result < 0) {
            toPrint("error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122");
        }
    }

    public void pause() {
        mSpeechSynthesizer.pause();
    }

    public void resume() {
        mSpeechSynthesizer.resume();
    }

    public void stop() {
        count = 0;
        isSpeaking = false;
        mSpeechSynthesizer.stop();
    }

    private void toPrint(String str) {
        Log.w(TAG, str);
    }
}
