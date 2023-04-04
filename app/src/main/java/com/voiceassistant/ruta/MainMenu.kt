package com.voiceassistant.ruta

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainMenu : AppCompatActivity(), Recognizer.OnResultsListener {
    private lateinit var editText: EditText
    private lateinit var micButton: ImageButton
    private lateinit var recognizer: Recognizer
    private lateinit var ttsBtn: Button
    private lateinit var speech: Speech




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        if (isPermissionGranted) {
            requestPermission()
        }
        editText = findViewById(R.id.edtSpeechText)
        micButton = findViewById(R.id.imgBtnMic)
        recognizer = Recognizer(this, this)
        speech = Speech(this)
        ttsBtn = findViewById(R.id.ttsBtn)
        recognizer.startListening()

        micButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> recognizer.stopListening()
                MotionEvent.ACTION_DOWN -> {
                    micButton.setImageResource(R.drawable.ic_mics)
                    recognizer.startListening()

                }


            }
            false
        }

        ttsBtn.setOnClickListener {
            Log.i("Speech text",editText.text.toString())
           speech.speak(editText.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recognizer.destroy()
    }

    override fun onResults(result: String) {

        micButton.setImageResource(R.drawable.ic_mic_off)
        editText.setText(result)
        if (result.toString() == "Слава Україні"){
            speech.speak("Героям Слава")
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_RECORD_AUDIO_REQUEST && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    private val isPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                MainMenu.Companion.PERMISSION_RECORD_AUDIO_REQUEST
            )
        }
    }

    companion object {
        private const val PERMISSION_RECORD_AUDIO_REQUEST = 1
    }
}





