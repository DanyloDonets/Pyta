package com.voiceassistant.ruta.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.voiceassistant.ruta.R




class Account : AppCompatActivity() {

    lateinit var exitBtn : Button
    lateinit var changeBtn : Button
    lateinit var mainMenu : ImageView
    lateinit var settings : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        exitBtn = findViewById(R.id.exitBtn)
        changeBtn = findViewById(R.id.changeBtn)
        mainMenu = findViewById(R.id.chat_btn)
        settings = findViewById(R.id.settings_btn)
        exitBtn.setOnClickListener{
            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("logined", false)
            editor.apply()
            editor.commit()
            setContentView(R.layout.activity_login)
            val intent = Intent(this@Account, Login::class.java)
            startActivity(intent)
            finish()
        }
        changeBtn.setOnClickListener {
            setContentView(R.layout.activity_change_pass)
            val intent = Intent(this@Account, ChangePass::class.java)
            startActivity(intent)
        }
        mainMenu.setOnClickListener {
            setContentView(R.layout.activity_main_menu)
            val intent = Intent(this@Account, MainMenu::class.java)
            startActivity(intent)
        }
        settings.setOnClickListener {
            setContentView(R.layout.activity_settings)
            val intent = Intent(this@Account, Settings::class.java)
            startActivity(intent)
        }

    }
}