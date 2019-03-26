package io.zcx.plugin.wrapper.bazel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.launcher.ARouter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.micro.android_library_module.UserCenterActivity;
import io.micro.butterknife_module.ButterKnifeModuleActivity;
import io.micro.java_module.JavaModule;
import io.micro.kotlin_module.KotlinModuleActivity;
import io.micro.kotlin_module.util.ToastsKt;
import io.micro.module1.Utils;
import io.zcx.plugin.wrapper.bazel.arouter.RoutePaths;
import io.zcx.plugin.wrapper.bazel.butterknife.ButterKnifeActivity;
import io.zcx.plugin.wrapper.bazel.widget.RecyclerItemClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initARouter();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Utils.showToast("hello");
        JavaModule.run();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_type1, viewGroup, false);
                //@formatter:off
                return new RecyclerView.ViewHolder(inflate) {};
                //@formatter:on
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClick(recyclerView,
                new RecyclerItemClick.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Utils.showToast("You click " + position);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        ToastsKt.longToast(MainActivity.this, "onItemLongClick " + position);
                    }
                }));
    }

    private void initARouter() {
        ARouter.openLog();
        ARouter.openDebug();
        LogisticsCenter.setExtraDexList(collectExtraDexPath("/data/local/tmp/incrementaldeployment/" + getPackageName() + "/dex"));
        ARouter.init(getApplication());
    }

    private static List<String> collectExtraDexPath(String... extraDexDir) {
        List<String> extraDexPaths = Collections.emptyList();
        if (extraDexDir != null) {
            extraDexPaths = new ArrayList<>();

            for (String dir : extraDexDir) {
                File dexDir = new File(dir);
                if (dexDir.exists() && dexDir.isDirectory()) {
                    File[] listFiles = dexDir.listFiles();
                    for (File dexFile : listFiles) {
                        extraDexPaths.add(dexFile.getAbsolutePath());
                    }
                }
            }
        }
        return extraDexPaths;
    }


    public void gotoAndroidLibraryModule(View view) {
        startActivity(new Intent(this, UserCenterActivity.class));
    }

    public void gotoKotlinActivity(View view) {
        startActivity(new Intent(this, KotlinActivity.class));
    }

    public void gotoKotlinModule(View view) {
        startActivity(new Intent(this, KotlinModuleActivity.class));
    }

    public void gotoButterKnife(View view) {
        startActivity(new Intent(this, ButterKnifeActivity.class));
    }

    public void gotoSecond(View view) {
        ARouter.getInstance()
                .build(RoutePaths.PAGE_AROUTER)
                .navigation();
    }

    public void gotoButterKnifeModule(View view) {
        startActivity(new Intent(this, ButterKnifeModuleActivity.class));
    }
}
