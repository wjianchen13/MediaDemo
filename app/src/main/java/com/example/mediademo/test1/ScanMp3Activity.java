package com.example.mediademo.test1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediademo.R;
import com.example.mediademo.utils.PermissionUtils;
import com.hjq.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class ScanMp3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_mp3);
    }


    public void onTest1(View v) {
        PermissionUtils.reqPermission(this, new PermissionUtils.OnPermissionResultListener() {
            @Override
            public void onGrated(Context context, @NonNull List<String> permissions) {
                super.onGrated(context, permissions);
//                scanMp3File();
                findLargeDurationMP3Files();
            }

            @Override
            protected void onResult(Context context, boolean isSuccess, @Nullable List<String> grantedPermissions, @NonNull List<String> deniedPermissions, @Nullable List<String> reqPermission) {
                super.onResult(context, isSuccess, grantedPermissions, deniedPermissions, reqPermission);
            }

            @Override
            public void onDenied(Context context, @Nullable List<String> grantedPermissions, @NonNull List<String> deniedPermissions, @Nullable List<String> reqPermission) {
                super.onDenied(context, grantedPermissions, deniedPermissions, reqPermission);
            }

            @Override
            public void onDenied(Context context, @Nullable List<String> grantedPermissions, @NonNull List<String> deniedPermissions, @Nullable List<String> reqPermission, boolean never) {
                super.onDenied(context, grantedPermissions, deniedPermissions, reqPermission, never);

            }
        }, Permission.READ_MEDIA_AUDIO);
    }

    private void scanMp3File() {
        new Thread(new Runnable() {
            @Override
            public void run() {



            }
        }).start();
    }

    public List<String> findLargeDurationMP3Files() {
        List<String> mp3Files = new ArrayList<>();

        // 构建查询
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION};
//        String selection = MediaStore.Audio.Media.SIZE + " > ? AND " + MediaStore.Audio.Media.DURATION + " > ?";
//        String[] selectionArgs = new String[]{"1048576", "60000"}; // 1MB = 1048576 bytes, 60 seconds = 60000 milliseconds
        String sortOrder = null;

        // 执行查询
        Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                if(index >= 0 ) {
                    String filePath = cursor.getString(index);
                    mp3Files.add(filePath);
                    System.out.println("==================> filePath: " + filePath);
                }
            }
            cursor.close();
        }

        return mp3Files;
    }
}