package com.isaquesoft.rastreiocorreios.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isaquesoft.rastreiocorreios.databinding.ActivityEncomendasBinding

class EncomendasActivity : AppCompatActivity(){

    private val binding by lazy {
        ActivityEncomendasBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

}