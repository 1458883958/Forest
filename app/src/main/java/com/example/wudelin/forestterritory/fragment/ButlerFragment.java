package com.example.wudelin.forestterritory.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.entity.VoiceBean;
import com.example.wudelin.forestterritory.entity.WS;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.fragment
 * 创建者：   wdl
 * 创建时间： 2018/4/7 21:16
 * 描述：    语音控制
 */

public class ButlerFragment extends Fragment{
    private Button btnTTS;
    private  StringBuffer strbuf;
    private TextView tvTTs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        tvTTs = view.findViewById(R.id.tts_result);
        btnTTS = view.findViewById(R.id.btn_tts);
        btnTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},0);
        }else {
           startSpeed();
        }
    }

    private void startSpeed() {
        //创建语音识别对话框
        RecognizerDialog rd = new RecognizerDialog(getActivity(),null);
        //设置参数accent,language等参数
        rd.setParameter(SpeechConstant.LANGUAGE,"zh_cn");//中文
        rd.setParameter(SpeechConstant.ACCENT,"mandarin");//普通话
        strbuf = new StringBuffer();
        //设置回调接口
        rd.setListener(new RecognizerDialogListener() {

            @Override
            public void onResult(RecognizerResult arg0, boolean arg1) {
                // TODO Auto-generated method stub
                String  result = arg0.getResultString();
                Logger.e(result);

                String data = parseJson(result);
                strbuf.append(data);
                tvTTs.append(strbuf+"\n");
                if(arg1){//回话结束
                    String voice = strbuf.toString();
                    Logger.e(voice);
                }

                Logger.e(arg1+"");
            }

            @Override
            public void onError(SpeechError arg0) {
                // TODO Auto-generated method stub

            }
        });
        //显示对话框
        rd.show();
    }

    /*
    * json解析
    *
    * */
    private String parseJson(String result) {
        Gson gson = new Gson();
        VoiceBean bean = gson.fromJson(result,VoiceBean.class);
        ArrayList<WS> ws = (ArrayList<WS>) bean.getWsList();
        StringBuffer sb = new StringBuffer();
        for (WS ws2 : ws) {
            String info = ws2.getCwList().get(0).getW();
            sb.append(info);
        }
        return sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            switch (requestCode){
                case 0:
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                            startSpeed();
                    }else {
                        ToastUtil.showByStr(getActivity(),getString(R.string.no_permission));
                    }
                    break;
                default:
            }
        }
    }

}
