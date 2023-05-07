package com.voiceassistant.ruta.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.voiceassistant.ruta.*
import com.voiceassistant.ruta.commands.CheckCommands
import com.voiceassistant.ruta.databinding.ActivityMainMenuBinding
import com.voiceassistant.ruta.model.Message


class MainMenu : AppCompatActivity(), Recognizer.OnResultsListener {
    private lateinit var editText: EditText
    private  lateinit var CheckCommands:CheckCommands
    private lateinit var recognizer: Recognizer

    private lateinit var speech: Speech
    private lateinit var _binding : ActivityMainMenuBinding
    private lateinit var chatGptViewModel: ChatGptViewModel
    private lateinit var recyclerView:RecyclerView







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        chatGptViewModel= ChatGptViewModel()
        CheckCommands = CheckCommands()
        speech= Speech(this)
        _binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val binding = _binding.root

        setContentView(binding)


        if (isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            requestPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_RECORD_AUDIO_REQUEST)
        }

        if (isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            requestPermission(Manifest.permission.READ_CONTACTS, PERMISSION_READ_CONTACTS_REQUEST)
        }

        recognizer = Recognizer(applicationContext, this)

        recyclerView = findViewById(R.id.recycler_view)

        chatGptViewModel = ViewModelProvider(this)[ChatGptViewModel::class.java]

        val llm = LinearLayoutManager(this)
        _binding.recyclerView.layoutManager = llm

        chatGptViewModel.messageList.observe(this){messages ->
            val adapter = MessageAdapter(messages)
            _binding.recyclerView.adapter = adapter
        }

        _binding.micBtn.setOnClickListener {


            recognizer.startListening()



        }

       _binding.accountBtn.setOnClickListener {
            val intent = Intent(this@MainMenu, Account::class.java)
            startActivity(intent)
        }
           _binding.settingsBtn.setOnClickListener {
            val intent = Intent(this@MainMenu, Settings::class.java)
            startActivity(intent)
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        recognizer.destroy()
    }



    override fun onResults(result: String) {

        chatGptViewModel.addToChat(result, Message.SENT_BY_ME,chatGptViewModel.getCurrentTimestamp())
        CheckCommands.commands(result, chatGptViewModel,this,speech)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_RECORD_AUDIO_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission to record audio granted", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_READ_CONTACTS_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission to read contacts granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                requestCode
            )
        }
    }

    companion object {
        private const val PERMISSION_RECORD_AUDIO_REQUEST = 1
        private const val PERMISSION_READ_CONTACTS_REQUEST = 2
    }
}





