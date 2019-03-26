package io.zcx.plugin.wrapper.bazel.butterknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.ButterKnife;
import io.zcx.plugin.wrapper.bazel.R;

/**
 * Test AnnotationProcessor
 */
public class ButterKnifeActivity extends AppCompatActivity {

    //    @BindView(R.id.tv_test)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);

        ButterKnife.bind(this);
    }

    //    @OnClick(R.id.btn_test)
    public void clickMe() {
        textView.setText("I'm setup from here.");
    }
}
