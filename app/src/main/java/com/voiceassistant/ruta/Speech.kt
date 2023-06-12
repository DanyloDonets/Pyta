package com.voiceassistant.ruta

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import java.util.*

class Speech(private val context: Context) : TextToSpeech.OnInitListener {


    private var tts: TextToSpeech? = null
    private var ttsm: TextToSpeech? = null

    init {
        tts = TextToSpeech(context, this)
        ttsm = TextToSpeech(context, this)
    }

    fun speak(text: String) {
        if (tts != null) {
            val sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val voice = sharedPreferences.getBoolean("voiceType",false)
            if (voice == false) {
                tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            }
            else{
                ttsm!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")

            }
        } else {
            Log.e("TTS", "TextToSpeech not initialized")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

                val result = tts?.setLanguage(Locale("uk", "UA"))
                val resultm = ttsm?.setLanguage(Locale("uk", "UA"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS","The Language not supported!")
                }
            } else {
                Log.e("TTS", "TextToSpeech initialization failed")
            }


            tts?.voice = Voice("uk-ua-x-ism#female_1-local", Locale("uk", "UA"), Voice.QUALITY_HIGH, Voice.LATENCY_NORMAL, false, null)
            ttsm?.voice = Voice("uk-ua-x-ism#male_1-local", Locale("uk", "UA"), Voice.QUALITY_HIGH, Voice.LATENCY_NORMAL, false, null)



    }




    fun onDestroy() {
        // Shutdown TTS when activity is destroyed
        tts?.stop()
        tts?.shutdown()
        ttsm?.stop()
        ttsm?.shutdown()
    }

}