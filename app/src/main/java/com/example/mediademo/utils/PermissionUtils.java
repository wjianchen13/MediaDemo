package com.example.mediademo.utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.hjq.permissions.IPermissionInterceptor;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.PermissionFragment;
import com.hjq.permissions.XXPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionUtils {
    public static class EmptyInterceptor implements IPermissionInterceptor {
        @Override
        public void requestPermissions(Activity activity, List<String> allPermissions, OnPermissionCallback callback) {
            PermissionFragment.beginRequest(activity, new ArrayList<>(allPermissions), this, callback);
        }
    }

    private interface IOnPermissionResultListener<T> {
        void onGrated(Context context, @NonNull T permission);

        void onDenied(Context context, @Nullable T grantedPermissions, @NonNull T deniedPermissions, @Nullable T reqPermission);
    }

    public static class OnPermissionResultListener implements IOnPermissionResultListener<List<String>> {
        private boolean isNecessary;

        public OnPermissionResultListener() {
            this(true);
        }

        public OnPermissionResultListener(boolean isNecessary) {
            this.isNecessary = isNecessary;
        }

        @SafeVarargs
        private final boolean containsReadStoragePermission(List<String>... permissions) {
            if (permissions != null && permissions.length > 0) {
                for (List<String> permissionList : permissions) {
                    if (permissionList!=null){
                        if (permissionList.contains(Permission.READ_EXTERNAL_STORAGE) || permissionList.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @CallSuper
        protected void onResult(Context context, boolean isSuccess, @Nullable List<String> grantedPermissions,
                                @NonNull List<String> deniedPermissions, @Nullable List<String> reqPermission) {
            if (containsReadStoragePermission(grantedPermissions, deniedPermissions, reqPermission)) {

            }
        }


        @Override
        public void onGrated(Context context, @NonNull List<String> permissions) {
            onResult(context, true, permissions, null, permissions);
        }

        @Override
        public void onDenied(Context context, @Nullable List<String> grantedPermissions, @NonNull List<String> deniedPermissions, @Nullable List<String> reqPermission) {
            onResult(context, false, grantedPermissions, deniedPermissions, reqPermission);
            if (isNecessary) {

            }
        }

        public void onDenied(Context context, @Nullable List<String> grantedPermissions, @NonNull List<String> deniedPermissions, @Nullable List<String> reqPermission,boolean never) {
            onDenied(context, grantedPermissions, deniedPermissions, reqPermission);
        }


//        public String getHasAlwaysDeniedMsg(Context context, @NonNull List<String> permissions) {
//            List<String> permissionNames = Permission.transformText(context, permissions);
//            return "";
//        }
    }

    public static final int REQ_CODE_PERMISSION = 1234;

    private static IPermissionInterceptor mInterceptor = new EmptyInterceptor();

    public static boolean isGranted(Context context, String... permissions) {
        return XXPermissions.isGranted(context, permissions);
    }

    public static void reqLocationPermission(Context context, OnPermissionResultListener listener) {
        if (listener != null) {
            listener.isNecessary = false;
        }
        reqPermission(context, listener, Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION);
    }

//    public static void reqExternalStoragePermission(Context context, OnPermissionResultListener listener) {
//        if (listener != null) {
//            listener.isNecessary = false;
//        }
//        reqPermission(context, listener, Permission.Group.STORAGE);
//    }

//    public static void reqCamAudPermission(Context context, OnPermissionResultListener listener) {
//        if (listener != null) {
//            listener.isNecessary = false;
//        }
//        reqPermission(context, listener, Permission.CAMERA, Permission.RECORD_AUDIO);
//    }
//
//    public static void reqAudioPermission(Context context, OnPermissionResultListener listener) {
//        if (listener != null) {
//            listener.isNecessary = false;
//        }
//        reqPermission(context, listener, Permission.RECORD_AUDIO);
//    }
//
//    public static void reqCameraPermission(Context context, OnPermissionResultListener listener) {
//        if (listener != null) {
//            listener.isNecessary = false;
//        }
//        reqPermission(context, listener, Permission.CAMERA);
//    }
//
//
//    public static void reqPhoneStatePermission(Context context, OnPermissionResultListener listener) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            reqPermission(context, listener, Permission.READ_PHONE_STATE);
//        } else {
//            listener.onGrated(context, null);
//        }
//    }

    public static void reqPermission(Context context, OnPermissionResultListener listener, String... permissions) {
        reqPermission(context, listener,null, false, Arrays.asList(permissions));
    }

    public static void reqPermission(Context context, OnPermissionResultListener listener, IPermissionInterceptor rationale,
                                     boolean alwaysShowRationale, @NonNull List<String> permissions) {
//        permissions = checkExternalStorage(listener, permissions);

        if (!checkProtocal(context, listener, permissions)) {
            return;
        }

        if (permissions.isEmpty()) {
            if (listener != null) {
                listener.onGrated(context, permissions);
            }
            return;
        }

        //已有权限
        List<String> grantedPermissions = new ArrayList<>();
        //被拒绝权限
        List<String> deniedPermissions = new ArrayList<>();
        //等待申请权限
        List<String> reqPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (XXPermissions.isGranted(context, permission)) {
                grantedPermissions.add(permission);
                continue;
            }
//            long reqTime = sdkShareData.getReqPermsiionTime(permission);
//            //已被永久拒绝的权限不再申请，从未申请过的权限似乎hasAlwaysDeniedPermission一直返回true
//            if (context instanceof  Activity && reqTime > 0 && XXPermissions.isPermanentDenied((Activity) context, permission)) {
//                deniedPermissions.add(permission);
//                continue;
//            }
//            long curTimeMillis = System.currentTimeMillis();
//            long permissionRate = sdkShareData.getPermissionRate(permission);
//            if (permissionRate > 0) {//权限申请有间隔限制
//                if (curTimeMillis - reqTime < permissionRate) {
//                    deniedPermissions.add(permission);
//                    continue;
//                }
//            }
            reqPermissions.add(permission);
        }

        if (reqPermissions.size() > 0) {
            XXPermissions.with(context)
                    .permission(reqPermissions.toArray(new String[0]))
                    //首次拒绝不提示这个。之后再申请（如果系统没有判断为不再提示），可能出发再次申请/取消
                    //但是某些系统，如华为，首次拒绝即怕判断为不再提示，下次再申请也不会提示这个
                    //这是在listener下根据情况触发跳转设置提示
                    //这个提示在源码层根据是否弹出系统权限申请为依据是否弹出自己
                            .interceptor(alwaysShowRationale ? new EmptyInterceptor() : rationale != null ? rationale : mInterceptor)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (listener != null) {
                                grantedPermissions.addAll(permissions);
                                if (deniedPermissions.size() > 0) {
                                    listener.onDenied(context, grantedPermissions, deniedPermissions, null);
                                } else {
                                    listener.onGrated(context, grantedPermissions);
                                }

                            }
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            long curTimeMillis = System.currentTimeMillis();
                            if (listener != null) {
                                reqPermissions.removeAll(permissions);
                                grantedPermissions.addAll(reqPermissions);
                                deniedPermissions.addAll(permissions);
                                listener.onDenied(context, grantedPermissions, deniedPermissions, permissions, never);
                            }
                        }
                    });
        } else if (listener != null) {
            if (deniedPermissions.size() > 0) {
                listener.onDenied(context, grantedPermissions, deniedPermissions, null);
            } else {
                listener.onGrated(context, grantedPermissions);
            }
        }

    }

    private static List<String> checkExternalStorage(OnPermissionResultListener listener, @NonNull List<String> perList) {
        List<String> permissions = new ArrayList<>(perList);
        if (listener == null || !listener.isNecessary) {
            try {
                permissions.remove(Permission.READ_EXTERNAL_STORAGE);
                permissions.remove(Permission.WRITE_EXTERNAL_STORAGE);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return permissions;
    }

    private static <T> boolean checkProtocal(Context context, IOnPermissionResultListener<T> listener, T permission) {
        return true;
    }

}
