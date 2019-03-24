package io.zcx.plugin.wrapper.bazel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.micro.module1.Utils

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        Utils.showToast("Hello kotlin, I'm here.")
    }
}
