package io.micro.butterknife_module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ButterKnifeModuleActivity extends AppCompatActivity {

    @BindView(R2.id.tv_butterknife_text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife_module);

        ButterKnife.bind(this);
    }

    @OnClick(R2.id.btn_butterknife_go)
    public void gooo() {
        textView.setText("set text from here");
    }
}
