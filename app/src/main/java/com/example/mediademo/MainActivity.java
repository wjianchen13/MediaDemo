package com.example.mediademo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediademo.test1.ScanMp3Activity;
import com.example.mediademo.test1.ScanMp3Activity2;
import com.example.mediademo.test1.ScanMp3Activity3;

/**
 * 多媒体操作
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 扫描MP3文件
     * @param v
     */
    public void onTest1(View v) {
        startActivity(new Intent(this, ScanMp3Activity.class));
    }

    /**
     * 扫描某个文件夹MP3文件
     * @param v
     */
    public void onTest2(View v) {
        startActivity(new Intent(this, ScanMp3Activity2.class));
    }

    /**
     * 获取mp3封面信息
     * @param v
     */
    public void onTest3(View v) {
        startActivity(new Intent(this, ScanMp3Activity3.class));
    }

}