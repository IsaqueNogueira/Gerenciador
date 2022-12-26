package com.isaquesoft.rastreiocorreios.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import com.isaquesoft.rastreiocorreios.R

fun Fragment.verificaConexao() {
    val manager =
        activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkInfo = manager.activeNetworkInfo

    if (null != networkInfo) {
//        if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
//            Toast.makeText(this, "Wifi conectado", Toast.LENGTH_SHORT).show()
//
//        } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
//            Toast.makeText(this, "Dados móveis conectado", Toast.LENGTH_SHORT).show()
//        }
    } else {

        AlertDialog.Builder(context)
            .setView(R.layout.alert_dialog_sem_conexao)
            .setPositiveButton("Ok") {_,_->

            }
            .show()

        }
}