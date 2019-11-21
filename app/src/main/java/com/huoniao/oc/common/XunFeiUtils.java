package com.huoniao.oc.common;

import android.os.Bundle;

import com.huoniao.oc.MyApplication;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/2/4.
 */

public class XunFeiUtils implements InitListener, SynthesizerListener {
    private static volatile XunFeiUtils instance = null;
    private boolean isInitSuccess = false;
    private static SpeechSynthesizer mTts;
    private List<String> list = new ArrayList<>();
    private  boolean isNumber = true;
    private XunFeiUtils(){
        init();
    }
    //单例模式
    public static XunFeiUtils getInstance() {
        if (instance == null) {
            synchronized (XunFeiUtils.class) {
                if (instance == null) {
                    instance = new XunFeiUtils();
                }
            }
        }
        return instance;
    }
    // 初始化合成对象
    public void init() {


        if(mTts==null) {
            try {
                SpeechUtility.createUtility(MyApplication.mContext, SpeechConstant.APPID + "=5a7675a8"); //初始化讯飞语音
                mTts = SpeechSynthesizer.createSynthesizer(MyApplication.mContext, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            mTts.setParameter(SpeechConstant.PARAMS, null);  // 清空参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);  // 设置在线云端
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");  // 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");  // 设置发音语速
        mTts.setParameter(SpeechConstant.PITCH, "50"); // 设置音调
        mTts.setParameter(SpeechConstant.VOLUME, "100");   // 设置合成音量
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");  // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true"); // 设置播放合成音频打断音乐播放，默认为true

    }

    //开始合成
    public void speak(String msg) {
        list.add(msg);
        if (isNumber){
            isNumber = false;
            mTts.startSpeaking(list.get(0), this);
            list.remove(0);
        }
    }
    public void stop() {
        mTts.stopSpeaking();
    }

    @Override
    public void onEvent(int eventType, int i1, int i2, Bundle bundle) {
        if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            String sid = bundle.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
        }
    }
    @Override
    public void onInit(int code) {
        if (code == ErrorCode.SUCCESS) {
            isInitSuccess = true;
        }
    }
    @Override
    public void onSpeakBegin() { // 监听：开始播放

    }
    @Override
    public void onBufferProgress(int percent, int beginPos, int endPos,
                                 String info) { // 合成进度

    }
    @Override
    public void onSpeakPaused() {  // 暂停播放

    }
    @Override
    public void onSpeakResumed() { // 继续播放

    }
    @Override
    public void onSpeakProgress(int percent, int beginPos, int endPos) { // 播放进度

    }
    @Override
    public void onCompleted(SpeechError speechError) {
        if (speechError == null) {
            if(list.size()>0) {
                mTts.startSpeaking(list.get(0), this);
                list.remove(0);
            }else{
                isNumber = true;
                mTts.stopSpeaking();
            }

        }
    }
    public void pause() {
        mTts.pauseSpeaking();
    }

    public void resume() {
        mTts.resumeSpeaking();
    }

    public void release() {//退出时释放
        if (null != mTts) {
            mTts.stopSpeaking();
            mTts.destroy();
        }
    }
}
