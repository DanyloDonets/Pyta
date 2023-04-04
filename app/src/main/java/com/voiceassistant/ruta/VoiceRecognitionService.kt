package com.voiceassistant.ruta

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class VoiceRecognitionService : Service() {
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startListening()
    }

    public fun startListening() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val intentRecognizer = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        speechRecognizer.setRecognitionListener(listener)
        speechRecognizer.startListening(intentRecognizer)
    }

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.d(TAG, "onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.d(TAG, "onRmsChanged")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d(TAG, "onBufferReceived")
        }

        override fun onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech")
            startListening()
        }

        override fun onError(error: Int) {
            Log.e(TAG, "onError: $error")
            startListening()
        }

        override fun onResults(results: Bundle?) {
            Log.d(TAG, "onResults")
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.let {
                for (result in it) {
                    if (result.toLowerCase().contains("рута")) {
                        // Знайдено команду "Рута"
                        Log.d(TAG, "Found command: Рута")
                        // Викликати вашу функцію для розпізнавання тексту тут
                    }
                }
            }
            startListening()
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d(TAG, "onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d(TAG, "onEvent")
        }
    }

    companion object {
        private const val TAG = "VoiceRecognitionService"
    }
}