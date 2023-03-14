package com.voiceassistant.ruta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

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