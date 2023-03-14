package com.voiceassistant.ruta

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
        //val intent = Intent(this@MainActivity, Login::class.java)
        //startActivity(intent)
        btn = findViewById(R.id.btn)


        Log.e("Logining", "text")

        btn.setOnClickListener {
            Log.i("Click", "clicked")
            //regtext.text = "putin huylo"
            setContentView(R.layout.activity_login)
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
        }

    }
}