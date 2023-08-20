package com.billing.mybilling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.billing.mybilling.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}