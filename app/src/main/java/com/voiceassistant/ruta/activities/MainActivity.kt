package com.voiceassistant.ruta.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.voiceassistant.ruta.R

class MainActivity : AppCompatActivity() {
    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)


    }
}