package com.xyd.waveview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //录音权限
    private String[] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //申请录音权限
    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    this.finish();
                }
            }
        }
    }
    WaveView waveView;
    MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waveView = (WaveView) findViewById(R.id.waveView);

        handler = new MyHandler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!waveView.hasOver()){
                    Message msg =handler.obtainMessage();
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Random random = new Random();
            waveView.putValue(random.nextInt(100));
        }
    }
}
