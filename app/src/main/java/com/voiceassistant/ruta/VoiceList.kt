package com.voiceassistant.ruta

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.core.os.LocaleListCompat
import java.util.*

class VoiceList(context: Context) {
    private val tts: TextToSpeech = TextToSpeech(context, null)
    private val supportedVoices = mutableSetOf<Voice>()

    init {
        val defaultLocales = LocaleListCompat.getAdjustedDefault()
        for (i in 0 until defaultLocales.size()) {
            val locale = defaultLocales[i]
            val availableVoices = tts.voices.filter {
                it.locale == locale && it.quality >= Voice.QUALITY_NORMAL && !it.isNetworkConnectionRequired
            }
            supportedVoices.addAll(availableVoices)
        }
    }

    fun getMaleEnglishVoice(): Voice? {
        return supportedVoices.firstOrNull {
            it.locale == Locale.US && it.name.contains("male", true)
        }
    }

    fun getFemaleEnglishVoice(): Voice? {
        return supportedVoices.firstOrNull {
            it.locale == Locale.US && it.name.contains("female", true)
        }
    }

    fun getMaleUkrainianVoice(): Voice? {
        return supportedVoices.firstOrNull {
            it.locale == Locale("uk", "UA") && it.name.contains("male", true)
        }
    }

    fun getFemaleUkrainianVoice(): Voice? {
        return supportedVoices.firstOrNull {
            it.locale == Locale("uk", "UA") && it.name.contains("female", true)
        }
    }
}