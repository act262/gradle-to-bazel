package io.micro.databinding_module;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.micro.databinding_module.databinding.ActivityDatabindingModuleBinding;

public class DatabindingModuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDatabindingModuleBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding_module);

        UserModel userModel = new UserModel();
        userModel.name = "I'm databinding";
        userModel.age = 20;
        userModel.phone = "10086";
        userModel.desc = "description";

        dataBinding.setUser(userModel);

        dataBinding.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DatabindingModuleActivity.onClick");
            }
        });
    }
}
