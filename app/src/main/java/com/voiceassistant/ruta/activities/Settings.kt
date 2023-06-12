package com.voiceassistant.ruta.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.voiceassistant.ruta.R

class Settings : AppCompatActivity() {

    lateinit var mainMenu: ImageView
    lateinit var account: ImageView
    lateinit var voiceType : Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        voiceType = findViewById(R.id.voiceTypeSwitch)
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("voiceType")) {
            val voice = sharedPreferences.getBoolean("voiceType",false)
            if(voice == false){
                voiceType.isChecked = false
            }else{
                voiceType.isChecked = true
            }
        }
        else{
            val editor = sharedPreferences.edit()
            editor.putBoolean("voiceType",false)
            editor.apply()
            editor.commit()
        }

        mainMenu = findViewById(R.id.chat_btn)
        account = findViewById(R.id.account_btn)
        mainMenu.setOnClickListener {
            setContentView(R.layout.activity_main_menu)
            val intent = Intent(this@Settings, MainMenu::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            if (voiceType.isChecked){
                val editor = sharedPreferences.edit()
                editor.putBoolean("voiceType", true).apply()
            }else{
                editor.putBoolean("voiceType", false).apply()
            }
            finish()
        }
        account.setOnClickListener {
            setContentView(R.layout.activity_account)
            val intent = Intent(this@Settings, Account::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            if (voiceType.isChecked){
                val editor = sharedPreferences.edit()
                editor.putBoolean("voiceType", true).apply()
            }else{
                editor.putBoolean("voiceType", false).apply()
            }
            finish()
        }
    }
}