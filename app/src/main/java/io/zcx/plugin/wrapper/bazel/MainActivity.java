package io.zcx.plugin.wrapper.bazel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "world", Toast.LENGTH_SHORT).show();
    }
}
