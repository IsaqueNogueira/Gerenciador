package com.isaquesoft.rastreiocorreios.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.messaging.FirebaseMessaging
import com.isaquesoft.rastreiocorreios.databinding.ActivityEncomendasBinding
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.EstadoAppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EncomendasActivity : AppCompatActivity() {

    lateinit var mAdView: AdView
    private val estadoAppViewModel: EstadoAppViewModel by viewModel()
    private val binding by lazy {
        ActivityEncomendasBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        MobileAds.initialize(this)


        estadoAppViewModel.componentes.observe(this, Observer {
            if (it.appBar) {
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
            if (it.temBotaoBack) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }

        })

    }

}