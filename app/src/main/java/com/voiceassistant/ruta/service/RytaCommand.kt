package com.voiceassistant.ruta.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import java.util.*

class RytaCommand : Service() {

    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkAndStartListening()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        stopListening()
        super.onDestroy()
    }

    private fun checkAndStartListening() {
        if (isAppRunning()) {
            // Програма відкрита, не потрібно слухати голос
            stopSelf()
            return
        }



        startSpeechRecognition()
    }

    private fun isAppRunning(): Boolean {
        // Перевіряємо, чи ваша програма відкрита або запущена у фоновому режимі
        // Реалізуйте цю логіку згідно зі своїми потребами
        return false
    }

    private fun startSpeechRecognition() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )

        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // Викликається, коли розпізнавання готове до початку
            }

            override fun onBeginningOfSpeech() {
                // Викликається, коли користувач починає говорити
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Викликається, коли рівень голосу змінюється
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                TODO("Not yet implemented")
            }

            override fun onEndOfSpeech() {
                // Викликається, коли користувач закінчує говорити
            }

            override fun onError(error: Int) {
                // Викликається в разі помилки розпізнавання
            }

            override fun onResults(results: Bundle?) {
                // Викликається після отримання результатів розпізнавання
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val spokenText = matches[0]
                    if (spokenText.contains("рута", ignoreCase = true)) {
                        // Команда "рута" була розпізнана, виконуємо відповідну дію
                        openMyApp()
                    }
                }

                // Перевіряємо ще раз, чи програма відкрита
                checkAndStartListening()
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Викликається під час отримання проміжних результатів розпізнавання
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Викликається в разі спеціальних подій під час розпізнавання
            }
        }

        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    private fun stopListening() {
        speechRecognizer.destroy()
    }

    private fun openMyApp() {
        // Код для відкриття вашого додатку
        Toast.makeText(this, "Відкрито додаток!", Toast.LENGTH_SHORT).show()
    }



}