package ru.atol.barcodetest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent



abstract class BarcodeReceiver : BroadcastReceiver() {


    companion object {
        const val XCHENG_ACTION = "com.xcheng.scanner.action.BARCODE_DECODING_BROADCAST"
        const val XCHENG_EXTRA = "EXTRA_BARCODE_DECODING_DATA"
        const val XCHENG_TYPE_EXTRA = "EXTRA_BARCODE_DECODING_SYMBOLE"
    }

    private var context: Context? = null

    fun register(context: Context) {
        this.context = context
        val xchengIntentFilter = IntentFilter(XCHENG_ACTION)
        context.registerReceiver(this, xchengIntentFilter)
    }

    fun unregister(context: Context) {
        this.context = null
        context.unregisterReceiver(this)
    }

    fun register(lifecycleOwner: LifecycleOwner, context: Context) {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun registerContext() {
                register(context)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun unregisterContext() {
                unregister(context)
            }
        })
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onReceive(context: Context, intent: Intent) {
        when {
            intent.action == XCHENG_ACTION -> if (intent.hasExtra(XCHENG_EXTRA)) {
                val type = if (intent.hasExtra(XCHENG_TYPE_EXTRA))
                    intent.getStringExtra(XCHENG_TYPE_EXTRA)
                else
                    null

                val barcode = intent.getStringExtra(XCHENG_EXTRA)

                onBarcodeReceive(context, type ?: "N/A", barcode ?: "N/A")
            }

        }
    }

    protected abstract fun onBarcodeReceive(context: Context, type: String, barcode: String)

}
