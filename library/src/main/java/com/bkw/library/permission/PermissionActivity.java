package com.bkw.library.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.bkw.library.permission.core.IPermission;
import com.bkw.library.permission.utils.PermissionUtils;

public class PermissionActivity extends Activity {
    /**
     * 定义一个接受用户传递的权限标识
     */
    private final static String PARAM_PREMISSION = "param_permission";

    private final static String PARAM_REQUEST_CODE = "param_request_code";
    public final static int PARAM_REQUEST_CODE_DEFAULT = -1;

    /**
     * 权限集合
     */
    private String[] permissions;
    private int requestCode;
    /**
     * 申请处理回调
     */
    private static IPermission iPermissionListener;


    /**
     * 将当前Activity暴露给外界使用
     */
    public static void requestPermissionAction(Context context, String[] permissions, int requestCode, IPermission iPermission) {
        iPermissionListener = iPermission;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putStringArray(PARAM_PREMISSION, permissions);
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ac_permission);

        permissions = getIntent().getStringArrayExtra(PARAM_PREMISSION);
        requestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, PARAM_REQUEST_CODE_DEFAULT);

        if (permissions == null && requestCode < 0 && iPermissionListener == null) {
            this.finish();
            return;
        }


        //开始检查权限是否已授权
        boolean hasPermissionRequest = PermissionUtils.hasPermissionRequest(this, permissions);

        if (hasPermissionRequest) {
            //已经授权，无需再次申请,调用回调告知外界
            iPermissionListener.ganted();
            this.finish();
            return;
        }


        //如未授权，发起申请权限操作
        ActivityCompat.requestPermissions(this, permissions, requestCode);

    }

    /**
     * 申请权限之后的回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //验证申请权限的结果
        if (PermissionUtils.requestPermissionSuccess(grantResults)) {
            //通过监听接口，告知外界
            iPermissionListener.ganted();
            this.finish();
            return;
        }

        //如果用户拒绝了授权，（不再提示）等，告知外界
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
            //通过接口监听，告诉外界，被拒绝，(不再提示打勾)
            iPermissionListener.denied();
            this.finish();
            return;
        }

    }


    /**
     * 去除activity退出的动画效果
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
