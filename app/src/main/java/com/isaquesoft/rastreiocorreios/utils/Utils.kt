package com.isaquesoft.rastreiocorreios.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    fun dataHora(): String{
        val date = Calendar.getInstance().time
        var dateTimeFormat = SimpleDateFormat("d MMM yyyy HH:mm", Locale("PT", "BR"))
        val  data = dateTimeFormat.format(date)
        return data
    }

    fun data(): String{
        val date = Calendar.getInstance().time
        var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy", Locale("PT", "BR"))
        val  data = dateTimeFormat.format(date)
        return data
    }

    fun dataHoraMillisegundos(): Long {
        val cal = Calendar.getInstance()
        val dataMillesegundos =  cal.timeInMillis
        return dataMillesegundos
    }


    @SuppressLint("SuspiciousIndentation")
    fun dias(diaPostado: String, diaHoje : String): Int {
        val dataFormat = SimpleDateFormat("dd/MM/yyyy")
        val data1: Calendar = Calendar.getInstance()
        val data2: Calendar = Calendar.getInstance()

            data1.setTime(dataFormat.parse(diaPostado))
            data2.setTime(dataFormat.parse(diaHoje))
        Log.i("TAG", "dias: $data1 $data2")

        val dias = data2.get(Calendar.DAY_OF_YEAR) - data1.get(Calendar.DAY_OF_YEAR)

       return dias
    }

    fun formataDataConvertida(data: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = formatter.parse(data)
        var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("PT", "BR"))
        val  data = dateTimeFormat.format(date)
        return data
    }


}