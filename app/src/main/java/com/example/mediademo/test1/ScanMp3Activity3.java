package com.example.mediademo.test1;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediademo.R;
import com.example.mediademo.utils.PermissionUtils;
import com.example.mediademo.utils.Utils;
import com.hjq.permissions.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanMp3Activity3 extends AppCompatActivity {

    private Context context;
    private MediaScannerConnection mediaScannerConnection;
    private MediaScannerConnection.MediaScannerConnectionClient mediaScannerConnectionClient;
    private String filePath;
    private ImageView imgvCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_mp3_3);
        imgvCover = findViewById(R.id.imgv_cover);
        filePath = Environment.getExternalStorageDirectory().getPath();
    }


    public void onTest1(View v) {
        PermissionUtils.reqPermission(this, new PermissionUtils.OnPermissionResultListener() {
            @Override
            public void onGrated(Context context, @NonNull List<String> permissions) {
                super.onGrated(context, permissions);
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

    public List<String> findLargeDurationMP3Files() {
        List<String> mp3Files = new ArrayList<>();

        // 构建查询
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION};
        String selection = MediaStore.Audio.Media.SIZE + " > ? AND " + MediaStore.Audio.Media.DURATION + " > ?";
        String[] selectionArgs = new String[]{"90000", "40000"};; // 1MB = 1048576 bytes, 60 seconds = 60000 milliseconds
        String sortOrder = null;

        // 执行查询
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
//        Cursor cursor = getContentResolver().query(uri, null, null, null, sortOrder);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int index1 = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
                int index2 = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                if(index >= 0 ) {
                    String filePath = cursor.getString(index);
                    String size = cursor.getString(index1);
                    String duration = cursor.getString(index2);
                    if(TextUtils.isEmpty(filePath) || filePath.endsWith(".opus")) {
                        System.out.println("==================> error filePath: " + filePath + "  size: " + size + "  duration: " + duration);
                        continue;
                    }
                    mp3Files.add(filePath);
                    loadingCover(filePath);
                    System.out.println("==================> filePath: " + filePath + "  size: " + size + "  duration: " + duration);
                }
            }
            cursor.close();
        }

        return mp3Files;
    }

    /**
     * 加载封面
     * @param mediaUri MP3文件路径
     */
    private void loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        if(picture != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            if (bitmap != null) {
                Utils.log("成功 " + mediaUri);
                imgvCover.setImageBitmap(bitmap);
            }
        }
    }

}