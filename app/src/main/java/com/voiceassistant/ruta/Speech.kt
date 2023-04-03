package com.voiceassistant.ruta

import android.speech.tts.TextToSpeech
import java.util.*


class Speech {
    private lateinit var  tts: TextToSpeech

    fun speak(text: String) {
        val locale = Locale.getDefault()
        tts.language = locale

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}