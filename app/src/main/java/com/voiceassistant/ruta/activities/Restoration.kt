package com.voiceassistant.ruta.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.voiceassistant.ruta.R

class Restoration : AppCompatActivity() {

    lateinit var btnBack: Button
    lateinit var btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restoration)
        btnBack = findViewById(R.id.backBtn)
        btnSend = findViewById(R.id.ResBtn)
        Log.e("Logining", "text")
        btnSend.setOnClickListener {
            setContentView(R.layout.activity_login)
            val intent = Intent(this@Restoration, Login::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            setContentView(R.layout.activity_login)
            val intent = Intent(this@Restoration, Login::class.java)
            startActivity(intent)
        }
    }
}