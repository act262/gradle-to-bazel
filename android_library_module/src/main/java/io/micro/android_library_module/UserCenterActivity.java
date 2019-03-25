package io.micro.android_library_module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.micro.module1.Utils;


public class UserCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        Utils.showToast("Hi, I'm from Android library module.");
    }
}
