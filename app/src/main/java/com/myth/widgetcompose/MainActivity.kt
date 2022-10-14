package com.myth.widgetcompose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.input)
        val seek = findViewById<SeekBar>(R.id.seek)
        val bubble = findViewById<TextView>(R.id.bubble)

        WidgetComposeCentral().apply {
            widgetCompose().input = input
            widgetCompose().seek = seek
            widgetCompose().bubble = bubble
            addInterceptor(InputSeekInterceptor())
            addInterceptor(InputBubbleInterceptor())
            setup()
        }
    }
}