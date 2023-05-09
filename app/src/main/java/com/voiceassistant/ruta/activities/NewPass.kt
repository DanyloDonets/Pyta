package com.voiceassistant.ruta.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.voiceassistant.ruta.R

class NewPass : AppCompatActivity() {

    lateinit var btnBack: Button
    lateinit var btnReg: Button
    lateinit var code: EditText
    lateinit var password: EditText
    lateinit var confirmPass: EditText

    lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pass)

        code = findViewById(R.id.Code)
        password = findViewById(R.id.Pass)
        confirmPass = findViewById(R.id.CPass)
        btnBack = findViewById(R.id.backBtn)
        btnReg = findViewById(R.id.RegBtn)
        database = FirebaseDatabase.getInstance("https://ryta-b6998-default-rtdb.europe-west1.firebasedatabase.app/").getReference()

        btnBack.setOnClickListener {
            setContentView(R.layout.activity_login)
            val intent = Intent(this@NewPass, Login::class.java)
            startActivity(intent)
        }
    }
}