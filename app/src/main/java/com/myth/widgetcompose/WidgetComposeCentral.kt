package com.myth.widgetcompose

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import kotlin.concurrent.fixedRateTimer

class WidgetComposeCentral {

    private val chain = Chain()

    fun widgetCompose(): WidgetCompose {
        return chain.widgetCompose
    }

    fun addInterceptor(interceptor: Interceptor) {
        chain.addInterceptor(interceptor)
    }

    fun setup() {
        chain.process()
    }
}

class WidgetCompose {
    var input: EditText? = null
    var seek: SeekBar? = null
    var bubble: TextView? = null
}

class Chain {
    var widgetCompose: WidgetCompose = WidgetCompose()
    private var interceptors = mutableListOf<Interceptor>()

    fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    fun process() {
        interceptors.forEach {
            it.process(this)
        }
    }
}

interface Interceptor {
    fun process(chain: Chain)
}

class InputSeekInterceptor() : Interceptor {
    override fun process(chain: Chain) {
        val wc = chain.widgetCompose
        val seek = wc.seek ?: return
        val input = wc.input ?: return

        var seeking = false
        var inputing = false

        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (inputing) return
                input.setText(p1.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                seeking = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                seeking = false
            }
        })

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputing = true
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (seeking) return
                seek.progress = try {
                    p0.toString().toInt()
                } catch (ignore: Exception) {
                    0
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                inputing = false
            }
        })
    }
}

class InputBubbleInterceptor : Interceptor {

    override fun process(chain: Chain) {
        val wc = chain.widgetCompose
        val input = wc.input ?: return
        val bubble = wc.bubble ?: return


        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val progress = try {
                    p0.toString().toInt()
                } catch (ignore: Exception) {
                    0
                }.coerceAtMost(100)

                if (progress == 0) {
                    bubble.visibility = View.VISIBLE
                    bubble.text = "Min amount"
                } else if (progress == 100) {

                    bubble.visibility = View.VISIBLE
                    bubble.text = "Max amount"
                } else {
                    bubble.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
}
