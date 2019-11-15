package com.bkw.library.permission.core;

public interface IPermission {

    /**
     * 已经授权
     */
    void ganted();

    /**
     * 取消授权
     */
    void cancel();

    /**
     * 拒绝授权
     */
    void denied();
}
