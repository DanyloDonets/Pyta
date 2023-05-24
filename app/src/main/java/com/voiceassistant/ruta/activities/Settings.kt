package com.voiceassistant.ruta.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.voiceassistant.ruta.R

class Settings : AppCompatActivity() {

    lateinit var mainMenu: ImageView
    lateinit var account: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mainMenu = findViewById(R.id.chat_btn)
        account = findViewById(R.id.account_btn)
        mainMenu.setOnClickListener {
            setContentView(R.layout.activity_main_menu)
            val intent = Intent(this@Settings, MainMenu::class.java)
            startActivity(intent)
        }
        account.setOnClickListener {
            setContentView(R.layout.activity_account)
            val intent = Intent(this@Settings, Account::class.java)
            startActivity(intent)
        }
    }
}