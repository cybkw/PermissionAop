package com.bkw.library.permission.aspectj;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.bkw.library.permission.PermissionActivity;
import com.bkw.library.permission.annotation.Permission;
import com.bkw.library.permission.annotation.PermissionCancel;
import com.bkw.library.permission.annotation.PermissionDenied;
import com.bkw.library.permission.core.IPermission;
import com.bkw.library.permission.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 处理权限注解的Aspect
 *
 * @author bkw
 */
@Aspect
public class PermissionAspect {

    /**
     * 切入点-  @Pointcut
     * TODO @annotation(permission)
     */
    @Pointcut("execution(@com.bkw.library.annotation.Permission * *(..)) && @annotation(permission)")
    public void initPointcut(Permission permission) {
    }


    /**
     * @return
     * @Around 围绕切入点加入需要执行的代码
     */
    @Around(value = "initPointcut(permission)")
    public void execution(final ProceedingJoinPoint joinPoint, Permission permission) {
        Context context = null;

        //得到当前切入的类
        final Object aThis = joinPoint.getThis();

        //初始化context，是否属于Context上下文级别
        if (aThis instanceof Context) {
            context = (Context) aThis;
        } else if (aThis instanceof Fragment) {
            context = ((Fragment) aThis).getContext();
        } else {
            Log.e("TAG", "切入失败");
        }

        //判断是否为null
        if (null == context || permission == null) {
            throw new IllegalArgumentException("aop context|| permission is null...");
        }

        //调用权限处理的Activity 申请 检测 处理权限操作
        final Context finalContext = context;
        PermissionActivity.requestPermissionAction(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void ganted() {
                //授权成功,让方法执行流程继续
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancel() {
                //被拒绝， 调用被@PermissionCancel注解的方法
                PermissionUtils.invokeAnnotation(aThis, PermissionCancel.class);
            }

            @Override
            public void denied() {
                //授权被拒绝，且不再提醒，调用被@PermissionDenied注解的方法
                PermissionUtils.invokeAnnotation(aThis, PermissionDenied.class);

                //跳转到设置界面，让用户手动授权
                PermissionUtils.startAndroidSettings(finalContext);
            }
        });
    }
}
