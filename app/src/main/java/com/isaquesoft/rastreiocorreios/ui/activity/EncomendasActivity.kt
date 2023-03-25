package com.isaquesoft.rastreiocorreios.ui.activity

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.ads.*
import com.isaquesoft.rastreiocorreios.databinding.ActivityEncomendasBinding
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.EstadoAppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EncomendasActivity : AppCompatActivity() {

    private val estadoAppViewModel: EstadoAppViewModel by viewModel()
    private val binding by lazy {
        ActivityEncomendasBinding.inflate(layoutInflater)
    }

    private lateinit var adView: AdView
    private var initialLayoutComplete = false

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.adViewContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        estadoAppViewModel.componentes.observe(
            this,
            Observer {
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
            },
        )

        MobileAds.initialize(this) {}

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("ABCDEF012345")).build(),
        )

        adView = AdView(this)
        binding.adViewContainer.addView(adView)
        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        binding.adViewContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
    }

    public override fun onPause() {
        adView.pause()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adView.resume()
    }

    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    private fun loadBanner() {
        adView.adUnitId = AD_UNIT_ID

        adView.setAdSize(adSize)

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }

    companion object {
        private val AD_UNIT_ID = "ca-app-pub-6470587668575312/5197141347"
    }
}
