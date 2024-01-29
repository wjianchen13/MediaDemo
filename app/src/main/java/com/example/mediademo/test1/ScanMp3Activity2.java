package com.example.mediademo.test1;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediademo.R;
import com.example.mediademo.utils.PermissionUtils;
import com.hjq.permissions.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanMp3Activity2 extends AppCompatActivity {

    private Context context;
    private MediaScannerConnection mediaScannerConnection;
    private MediaScannerConnection.MediaScannerConnectionClient mediaScannerConnectionClient;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_mp3_2);
        filePath = Environment.getExternalStorageDirectory().getPath();
    }


    public void onTest1(View v) {
        PermissionUtils.reqPermission(this, new PermissionUtils.OnPermissionResultListener() {
            @Override
            public void onGrated(Context context, @NonNull List<String> permissions) {
                super.onGrated(context, permissions);
//                scanMp3File();
                findLargeDurationMP3Files();
                startScan();
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

    public List<String> findLargeDurationMP3Files() {
        List<String> mp3Files = new ArrayList<>();

        this.mediaScannerConnectionClient = new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                // 扫描连接成功后，调用扫描目录的方法
                scanDirectory(Environment.getExternalStorageDirectory().getPath() + "/Music");
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                // 扫描完成后的回调方法
                // 在这里可以处理扫描到的文件信息或其他操作
                System.out.println("==================> filePath: " + path);
            }
        };

        return mp3Files;
    }

    public void startScan() {
        // 创建 MediaScannerConnection 对象并传入上下文和回调接口
        mediaScannerConnection = new MediaScannerConnection(this, mediaScannerConnectionClient);
        // 连接到媒体扫描服务
        mediaScannerConnection.connect();
    }


    private void scanDirectory(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    if (childFile.isDirectory()) {
                        // 如果是目录，则递归扫描子目录
                        scanDirectory(childFile.getPath());
                    } else {
                        // 如果是 MP3 文件，则通知媒体库扫描该文件
//                        if (childFile.getName().endsWith(".mp3")) {
                            mediaScannerConnection.scanFile(childFile.getPath(), null);
//                        }
                    }
                }
            }
        }
    }

}