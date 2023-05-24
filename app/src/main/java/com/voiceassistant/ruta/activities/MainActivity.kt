package com.voiceassistant.ruta.activities

import android.content.Context
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
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("logined")) {
            val logined = sharedPreferences.getBoolean("logined",false)
            if(logined == true){
                setContentView(R.layout.activity_main_menu)
                val intent = Intent(this@MainActivity, MainMenu::class.java)
                startActivity(intent)
            }
            else{
                setContentView(R.layout.activity_login)
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
            }
        } else {
            val editor = sharedPreferences.edit()
            editor.putBoolean("logined", false)
            editor.apply()
            editor.commit()
        }


            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)


    }
}