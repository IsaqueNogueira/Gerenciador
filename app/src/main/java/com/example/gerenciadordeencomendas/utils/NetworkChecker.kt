package com.example.gerenciadordeencomendas.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.example.gerenciadordeencomendas.R

fun AppCompatActivity.verificaConexao() {


    val manager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkInfo = manager.activeNetworkInfo

    if (null != networkInfo) {
//        if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
//            Toast.makeText(this, "Wifi conectado", Toast.LENGTH_SHORT).show()
//
//        } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
//            Toast.makeText(this, "Dados móveis conectado", Toast.LENGTH_SHORT).show()
//        }
    } else {

        AlertDialog.Builder(this)
            .setView(R.layout.alert_dialog_sem_conexao)
            .setPositiveButton("Sim") {_,_->
                recreate()
            }
            .setNegativeButton("Não"){_,_->}
            .show()

        }
}