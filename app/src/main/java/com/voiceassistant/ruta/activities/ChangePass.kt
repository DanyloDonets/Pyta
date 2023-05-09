package com.voiceassistant.ruta.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.voiceassistant.ruta.R
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class ChangePass : AppCompatActivity() {

    lateinit var btnBack: Button
    lateinit var btnSend: Button
    lateinit var pass: EditText
    lateinit var cpass:EditText
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)
        btnBack = findViewById(R.id.backBtn)
        btnSend = findViewById(R.id.ResBtn)
        pass = findViewById(R.id.Pass)
        cpass = findViewById(R.id.CPass)
        database = FirebaseDatabase.getInstance("https://ryta-b6998-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email","email")

        btnSend.setOnClickListener {
            if(pass.text.toString() == cpass.text.toString()){
                val newPass = encrypt(pass.text.toString())
                val query = database.child("Accounts").orderByChild("email").equalTo(email)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (userSnapshot in dataSnapshot.children) {
                            val key = userSnapshot.key
                            if (key != null) {
                                database.child("Accounts").child(key).child("password").setValue(newPass)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this@ChangePass,
                                            "Пароль змінено",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        setContentView(R.layout.activity_main_menu)
                                        val intent = Intent(this@ChangePass, MainMenu::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this@ChangePass,
                                            "Пароль не змінено, спробуйте ще раз",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Помилка при читанні з бази даних
                    }
                })

            }
            else{
                Toast.makeText(
                    this@ChangePass,
                    "Паролі не співпадають",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        btnBack.setOnClickListener {
            setContentView(R.layout.activity_main_menu)
            val intent = Intent(this@ChangePass, MainMenu::class.java)
            startActivity(intent)
        }
    }


    fun encrypt(strToEncrypt: String) :  String?
    {
        val secretKey = "tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="
        val salt = "QWlGNHNhMTJTQWZ2bGhpV3U=" // base64 decode => AiF4sa12SAfvlhiWu
        val iv = "bVQzNFNhRkQ1Njc4UUFaWA==" // base64 decode => mT34SaFD5678QAZX
        try
        {
            val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec)
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
        }
        catch (e: Exception)
        {
            println("Error while encrypting: $e")
        }
        return null
    }


}