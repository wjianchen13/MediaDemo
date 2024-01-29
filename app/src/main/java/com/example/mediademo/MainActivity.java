package com.example.mediademo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediademo.test1.ScanMp3Activity;
import com.example.mediademo.test1.ScanMp3Activity2;

/**
 * 多媒体操作
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTest1(View v) {
        startActivity(new Intent(this, ScanMp3Activity.class));
    }

    public void onTest2(View v) {
        startActivity(new Intent(this, ScanMp3Activity2.class));
    }

}