package com.voiceassistant.ruta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Login : AppCompatActivity() {
    lateinit var btn: Button
    lateinit var regtext: TextView
    lateinit var restext: TextView
    lateinit var image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn = findViewById(R.id.LoginBtn)
        regtext = findViewById(R.id.regestration)
        restext = findViewById(R.id.restoration)
        image = findViewById(R.id.LoginForm)
            //btn.visibility = View.VISIBLE
        Log.e("Logining", "text")
        val textReg = "Ще не маю акаунту, Реєстрація."
        val spannableStringReg = SpannableString(textReg)

        val clickableSpanReg = object : ClickableSpan() {
            override fun onClick(view: View) {
                setContentView(R.layout.activity_regestration)
            }
        }

        val startIndexReg = textReg.indexOf("Реєстрація")
        val endIndexReg = startIndexReg + "Реєстрація".length

        spannableStringReg.setSpan(clickableSpanReg, startIndexReg, endIndexReg, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        regtext.text = spannableStringReg
        regtext.movementMethod = LinkMovementMethod.getInstance()

        val textRes = "Відновити пароль, Тут."
        val spannableStringRes = SpannableString(textRes)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                setContentView(R.layout.activity_restoration)
            }
        }

        val startIndexRes = textRes.indexOf("Тут")
        val endIndexRes = startIndexRes + "Тут".length

        spannableStringRes.setSpan(clickableSpan, startIndexRes, endIndexRes, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        restext.text = spannableStringRes
        restext.movementMethod = LinkMovementMethod.getInstance()
        btn.setOnClickListener {
            Log.i("Click", "clicked")
            setContentView(R.layout.activity_main_menu)
            val intent = Intent(this@Login, MainMenu::class.java)
           startActivity(intent)
        }

    }
}