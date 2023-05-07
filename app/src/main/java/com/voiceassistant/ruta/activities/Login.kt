package com.voiceassistant.ruta.activities


import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.voiceassistant.ruta.R
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Login : AppCompatActivity() {
    lateinit var btn: Button
    lateinit var regtext: TextView
    lateinit var restext: TextView
    lateinit var image: ImageView
    lateinit var email: EditText
    lateinit var password : EditText
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn = findViewById(R.id.LoginBtn)
        regtext = findViewById(R.id.regestration)
        restext = findViewById(R.id.restoration)
        image = findViewById(R.id.LoginForm)
        email = findViewById(R.id.Email)
        password = findViewById(R.id.Password)
        database = FirebaseDatabase.getInstance("https://ryta-b6998-default-rtdb.europe-west1.firebasedatabase.app/").getReference()







        Log.e("Logining", "text")
        val textReg = "Ще не маю акаунту, Реєстрація."
        val spannableStringReg = SpannableString(textReg)

        val clickableSpanReg = object : ClickableSpan() {
            override fun onClick(view: View) {


                setContentView(R.layout.activity_regestration)
                val intent = Intent(this@Login, Regestration::class.java)
                startActivity(intent)
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
                val intent = Intent(this@Login, Restoration::class.java)
                startActivity(intent)
            }
        }

        val startIndexRes = textRes.indexOf("Тут")
        val endIndexRes = startIndexRes + "Тут".length

        spannableStringRes.setSpan(clickableSpan, startIndexRes, endIndexRes, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        restext.text = spannableStringRes
        restext.movementMethod = LinkMovementMethod.getInstance()
        btn.setOnClickListener {
            database.child("Accounts").orderByChild("email")
                .equalTo(email.text.toString().lowercase())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Log.i("snap",snapshot.value.toString())
                            for(snap :DataSnapshot in snapshot.getChildren()){
                                if(decrypt(snap.child("password").value.toString()) == password.text.toString()){
                                    Log.i("snap",snap.child("password").value.toString())
                                    Log.i("snap2",decrypt(snap.child("password").value.toString()).toString())
                                    setContentView(R.layout.activity_main_menu)
                                    val intent = Intent(this@Login, MainMenu::class.java)
                                    startActivity(intent)
                                }
                                else {
                                    Toast.makeText(
                                        this@Login,
                                        "Пароль не вірний",
                                        Toast.LENGTH_LONG
                                    ).show()


                                }
                            }



                        } else {
                            Toast.makeText(
                                this@Login,
                                "Ця електронна адреса не зареєстрована",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
        }


    }
    fun decrypt(strToDecrypt: String) :  String?
    {
        val secretKey = "tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="
        val salt = "QWlGNHNhMTJTQWZ2bGhpV3U=" // base64 decode => AiF4sa12SAfvlhiWu
        val iv = "bVQzNFNhRkQ1Njc4UUFaWA==" // base64 decode => mT34SaFD5678QAZX
        try
        {

            val ivParameterSpec =  IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec);
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return  String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
        }
        catch (e : Exception) {
            println("Error while decrypting: $e");
        }
        return null
    }
}