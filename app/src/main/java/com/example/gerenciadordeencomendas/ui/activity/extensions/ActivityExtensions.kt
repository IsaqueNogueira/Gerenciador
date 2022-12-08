package com.example.gerenciadordeencomendas.ui.activity.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.transacaoFragment(executa: FragmentTransaction.()-> Unit){
    val transaction = supportFragmentManager.beginTransaction()
    executa(transaction)
    transaction.commit()
}