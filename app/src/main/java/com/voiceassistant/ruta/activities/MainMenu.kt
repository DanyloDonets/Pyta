package com.voiceassistant.ruta.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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

    private lateinit var CheckCommands:CheckCommands
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

        checkPermissions()

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
           setContentView(R.layout.activity_account)
            val intent = Intent(this@MainMenu, Account::class.java)
            startActivity(intent)

        }
           _binding.settingsBtn.setOnClickListener {
               setContentView(R.layout.activity_settings)
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

    private val RECORD_AUDIO_PERMISSION_REQUEST = 1
    private val READ_CONTACTS_PERMISSION_REQUEST = 2
    private val CALL_PHONE_PERMISSION_REQUEST = 3
    private val SET_ALARM_PERMISSION_REQUEST = 4
    private val SCHEDULE_EXACT_ALARM_PERMISSION_REQUEST = 5
    private val VIBRATE = 6
    private  val ACCESS_NOTIFICATION_POLICY = 7



    // Функція для перевірки дозволів
    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SET_ALARM,
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        )

        val permissionsToRequest = mutableListOf<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), RECORD_AUDIO_PERMISSION_REQUEST)
        }
    }

    // Обробник результатів запиту дозволів
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RECORD_AUDIO_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Дозвіл на доступ до запису аудіо отримано
                    // Виконувати необхідні дії
                } else {
                    // Дозвіл на доступ до запису аудіо не отримано
                    // Обробка відсутності дозволу
                }
            }
            // Аналогічно обробляти інші запити дозволів
        }
    }
}





