package io.zcx.plugin.wrapper.bazel.arouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;

import io.micro.module1.Utils;
import io.zcx.plugin.wrapper.bazel.R;

@Route(path = RoutePaths.PAGE_AROUTER)
public class ARouterPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter_page);


        Utils.showToast("Hi, I'm from ARouterPageActivity");
    }
}
