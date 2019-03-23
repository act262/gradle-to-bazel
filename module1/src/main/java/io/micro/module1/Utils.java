package io.micro.module1;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {

    public static void showToast(String msg) {
        Toast.makeText(application, msg, Toast.LENGTH_SHORT).show();
    }

    private static Application application;

    static {
        try {
            @SuppressLint("PrivateApi")
            Class<?> atCls = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = atCls.getMethod("currentActivityThread");
            Object sCurrentActivityThread = currentActivityThreadMethod.invoke(null);
            Method getApplicationMethod = atCls.getMethod("getApplication");
            application = (Application) getApplicationMethod.invoke(sCurrentActivityThread);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
