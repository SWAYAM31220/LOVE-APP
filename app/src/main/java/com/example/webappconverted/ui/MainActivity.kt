package com.example.webappconverted.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.webappconverted.R

// MainActivity hosts NavHostFragment and acts as single-activity container
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
