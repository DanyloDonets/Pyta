package com.voiceassistant.ruta.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.voiceassistant.ruta.R
import java.util.*
import javax.mail.*

import javax.mail.internet.InternetAddress

import javax.mail.internet.MimeMessage




class Restoration : AppCompatActivity() {

    lateinit var btnBack: Button
    lateinit var btnSend: Button
    lateinit var auth: FirebaseAuth
    lateinit var email : EditText
    lateinit var cEmail : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restoration)
        auth = FirebaseAuth.getInstance()
        btnBack = findViewById(R.id.backBtn)
        btnSend = findViewById(R.id.ResBtn)
        email = findViewById(R.id.Email)
        cEmail = findViewById(R.id.Email2)
        Log.e("Logining", "text")
        btnSend.setOnClickListener {
            if(email.text.toString().lowercase() == cEmail.text.toString().lowercase()){
                val stringSenderEmail = "voiceassistantruta@gmail.com"
                val stringReceiverEmail = email.text.toString().lowercase()
                val stringPasswordSenderEmail = "Z6x6a6q617456@@"

                val stringHost = "smtp.gmail.com"

                val properties: Properties = System.getProperties()

                properties.put("mail.smtp.host", stringHost)
                properties.put("mail.smtp.port", "465")
                properties.put("mail.smtp.ssl.enable", "true")
                properties.put("mail.smtp.auth", "true")

                val session: Session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
                    }
                })

                val mimeMessage = MimeMessage(session)
                mimeMessage.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(stringReceiverEmail)
                )
                val random = (100_000..999_999).random()
                val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("resCode", random.toString())
                editor.apply()
                mimeMessage.subject = "Ruta voice assistant. Відновлення пароля"
                mimeMessage.setText("Добрий день, \n\nВаш код для відновлення пароля: "+sharedPreferences.getString("resCode","000000"))

                val thread = Thread {
                    try {
                        Transport.send(mimeMessage)
                    } catch (e: MessagingException) {
                        e.printStackTrace()
                    }
                }
                thread.start()

                setContentView(R.layout.activity_new_pass)
                val intent = Intent(this@Restoration, NewPass::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Електронні адреси не співпадають", Toast.LENGTH_LONG).show()
            }
        }

        btnBack.setOnClickListener {
            setContentView(R.layout.activity_login)
            val intent = Intent(this@Restoration, Login::class.java)
            startActivity(intent)
        }
    }
}