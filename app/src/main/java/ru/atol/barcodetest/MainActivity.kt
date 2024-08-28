package ru.atol.barcodetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.Manifest
import android.content.Context

class MainActivity : ComponentActivity() {
    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE)
            , PERMISSIONS_REQUEST_CODE)
    }


    override fun onPause() {
        super.onPause()
        barcodeReceiver.unregister(this)
    }

    override fun onResume() {
        super.onResume()
        barcodeReceiver.register(this)
    }

    var result: String = ""
    private val barcodeReceiver = object : BarcodeReceiver() {
        override fun onBarcodeReceive(context: Context, type:String, barcode: String) {

            result+="\n\r" + barcode

            println(result)
        }
    }
}