package com.voiceassistant.ruta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log


class Recognizer(private val context: Context, private val onResultsListener: OnResultsListener) {

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}

        override fun onError(error: Int) {
            when (error) {
                SpeechRecognizer.ERROR_AUDIO -> Log.e("SpeechRecognizer", "Audio recording error")
                SpeechRecognizer.ERROR_CLIENT -> Log.e("SpeechRecognizer", "Client side error")
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> Log.e("SpeechRecognizer", "Insufficient permissions error")
                SpeechRecognizer.ERROR_NETWORK -> Log.e("SpeechRecognizer", "Network error")
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> Log.e("SpeechRecognizer", "Network timeout error")
                SpeechRecognizer.ERROR_NO_MATCH -> Log.e("SpeechRecognizer", "No recognition result matched")
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Log.e("SpeechRecognizer", "Recognition service busy error")
                SpeechRecognizer.ERROR_SERVER -> Log.e("SpeechRecognizer", "Server error")
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Log.e("SpeechRecognizer", "Speech timeout error")
                else -> Log.e("SpeechRecognizer", "Unknown error")
            }
        }

        override fun onResults(results: Bundle?) {
            val resultList = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (resultList != null && resultList.isNotEmpty()) {
                onResultsListener.onResults(resultList[0])
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "uk-UA")
    }

    fun startListening() {
        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(recognizerIntent)
    }

    fun stopListening() {
        speechRecognizer.stopListening()
    }

    fun destroy() {
        speechRecognizer.destroy()
    }

    interface OnResultsListener {

        fun onResults(result: String)
    }
}