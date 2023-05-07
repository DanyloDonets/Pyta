package com.voiceassistant.ruta.activities


import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.voiceassistant.ruta.AccountData
import com.voiceassistant.ruta.R
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Regestration : AppCompatActivity() {

    lateinit var btnBack: Button
    lateinit var btnReg: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirmPass: EditText
    lateinit var hint : TextView
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regestration)
        email = findViewById(R.id.Email)
        password = findViewById(R.id.Pass)
        confirmPass = findViewById(R.id.CPass)
        btnBack = findViewById(R.id.backBtn)
        btnReg = findViewById(R.id.RegBtn)
        hint = findViewById(R.id.hint)
        database = FirebaseDatabase.getInstance("https://ryta-b6998-default-rtdb.europe-west1.firebasedatabase.app/").getReference()

        btnReg.setOnClickListener {

            if(isValidPassword(password.text.toString()) == false){
                hint.visibility = View.VISIBLE
                Toast.makeText(this, "Пароль введено не за вимогами", Toast.LENGTH_LONG).show()
            }
            else if(EmailValidator.isEmailValid(email.text.toString()) == false){
                Toast.makeText(this, "Електронна адреса введена невірно", Toast.LENGTH_LONG).show()
            }
            else if(password.text.toString() != confirmPass.text.toString()){
                Toast.makeText(this, "Паролі не співпадають", Toast.LENGTH_LONG).show()
            }

            else {
                database.child("Accounts").orderByChild("email").equalTo(email.text.toString().lowercase())
                    .addValueEventListener(object: ValueEventListener{
                       override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(this@Regestration, "Ця електронна адреса вже зареєстрована", Toast.LENGTH_LONG).show()
                            }else{
                                val acc = AccountData(email.text.toString().lowercase(),encrypt(password.text.toString()))
                                val accId = database.push().key!!
                                database.child("Accounts").child(accId).setValue(acc)
                                Toast.makeText(this@Regestration, "Реєстрація успішна", Toast.LENGTH_LONG).show()
                                setContentView(R.layout.activity_login)
                                val intent = Intent(this@Regestration, Login::class.java)
                                startActivity(intent)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })




            }
        }

        btnBack.setOnClickListener {
            setContentView(R.layout.activity_login)
            val intent = Intent(this@Regestration, Login::class.java)
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




    internal fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

        return true
    }

    class EmailValidator {
        companion object {
            @JvmStatic
            val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

            fun isEmailValid(email: String): Boolean {
                return EMAIL_REGEX.toRegex().matches(email);
            }
        }
    }


}


