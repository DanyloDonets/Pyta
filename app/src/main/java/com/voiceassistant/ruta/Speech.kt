package com.voiceassistant.ruta

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class Speech(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context, this)
    }

    fun speak(text: String) {
        if (tts != null) {
            tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Log.e("TTS", "TextToSpeech not initialized")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            }
        } else {
            Log.e("TTS", "TextToSpeech initialization failed")
        }
    }

    fun onDestroy() {
        // Shutdown TTS when activity is destroyed
        tts?.stop()
        tts?.shutdown()
    }
}