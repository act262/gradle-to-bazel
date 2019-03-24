package io.micro.kotlin_module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.micro.kotlin_module.util.toast

class KotlinModuleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_module)

        toast("I'm from kotlin module, Hello world!")
    }
}
