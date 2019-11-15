package com.bkw.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bkw.library.permission.annotation.Permission;
import com.bkw.library.permission.annotation.PermissionCancel;
import com.bkw.library.permission.annotation.PermissionDenied;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Permission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode = 200)
    public void testRequest(View view) {
        Log.e("TAG", "权限申请成功");
    }

    /**
     * 授权被取消
     */
    @PermissionCancel
    public void testCancel() {
        Log.e("TAG", "授权被拒绝。。。。");
    }

    @PermissionDenied
    public void testDenied() {
        Log.e("TAG", "授权被拒绝，用户勾选了不再提示。。。。");

    }

}
