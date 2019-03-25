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

import io.micro.android_library_module.UserCenterActivity;
import io.micro.java_module.JavaModule;
import io.micro.kotlin_module.KotlinModuleActivity;
import io.micro.module1.Utils;
import io.zcx.plugin.wrapper.bazel.widget.RecyclerItemClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                        gotoAndroidLibraryModule();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
//                        gotoKotlin();
                        gotoKotlinModule();
                    }
                }));
    }

    private void gotoKotlin() {
        startActivity(new Intent(this, KotlinActivity.class));
    }

    private void gotoKotlinModule() {
        startActivity(new Intent(this, KotlinModuleActivity.class));
    }

    private void gotoAndroidLibraryModule() {
        startActivity(new Intent(this, UserCenterActivity.class));
    }
}
