package com.voiceassistant.ruta

import android.Manifest
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
import com.voiceassistant.ruta.commands.CheckCommands
import com.voiceassistant.ruta.databinding.ActivityMainMenuBinding


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

        speech= Speech(this)
        _binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val binding = _binding.root

        setContentView(binding)
        speech.speak("Привіт")

        if (isPermissionGranted) {
            requestPermission()
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

        _binding.sendBtn.setOnClickListener {

            val question = _binding.messageEditText.text.toString()
            recognizer.startListening()



        }




    }

    override fun onDestroy() {
        super.onDestroy()
        recognizer.destroy()
    }



    override fun onResults(result: String) {


        CheckCommands.commands(result,this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_RECORD_AUDIO_REQUEST && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    private val isPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                MainMenu.Companion.PERMISSION_RECORD_AUDIO_REQUEST
            )
        }
    }

    companion object {
        private const val PERMISSION_RECORD_AUDIO_REQUEST = 1
    }
}





