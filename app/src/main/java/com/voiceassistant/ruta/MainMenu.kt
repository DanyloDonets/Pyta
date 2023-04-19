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
import com.voiceassistant.ruta.databinding.ActivityMainMenuBinding
import com.voiceassistant.ruta.model.Message


class MainMenu : AppCompatActivity(), Recognizer.OnResultsListener {
    private lateinit var editText: EditText
    //private lateinit var micButton: ImageButton
    private lateinit var recognizer: Recognizer
    //private lateinit var ttsBtn: Button
    private lateinit var speech: Speech
    private lateinit var _binding : ActivityMainMenuBinding
    private lateinit var chatGptViewModel: ChatGptViewModel
    private lateinit var recyclerView:RecyclerView
    //private  var Context:Context ? = null




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
       // editText = findViewById(R.id.edtSpeechText)
       // micButton = findViewById(R.id.imgBtnMic)

        recognizer = Recognizer(applicationContext, this)
       // speech = Speech(applicationContext)
        recyclerView = findViewById(R.id.recycler_view)
            //ttsBtn = findViewById(R.id.ttsBtn)





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
            chatGptViewModel.addToChat("Слухаю...", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())


            //chatGptViewModel.addToChat(question, Message.SENT_BY_ME,chatGptViewModel.getCurrentTimestamp())
            //_binding.messageEditText.setText("")
            //chatGptViewModel.callApi(question)
            //val adapter = recyclerView.adapter as MessageAdapter
            //val messages = adapter.itemCount
            //if (messages != 0) {
              //  val lastMessage = recyclerView.get(messages-1)
                //speech.speak(lastMessage.toString())
            //}



        }




        /*micButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> recognizer.stopListening()
                MotionEvent.ACTION_DOWN -> {
                    micButton.setImageResource(R.drawable.ic_mics)
                    recognizer.startListening()

                }


            }
            false
        }*/

       // ttsBtn.setOnClickListener {
         //   Log.i("Speech text",editText.text.toString())
           //speech.speak(editText.text.toString())
        //}
    }

    override fun onDestroy() {
        super.onDestroy()
        recognizer.destroy()
    }



    override fun onResults(result: String) {

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        chatGptViewModel.addToChat(result, Message.SENT_BY_ME,chatGptViewModel.getCurrentTimestamp())
        Toast.makeText(this, "1req", Toast.LENGTH_SHORT).show()
        //_binding.messageEditText.setText("")
        chatGptViewModel.callApi(result,speech)

        //speech.speak(result)
/*
        Toast.makeText(this, "2req", Toast.LENGTH_SHORT).show()
        val adapter = recyclerView.adapter as MessageAdapter
        val messages = adapter.itemCount
        if (messages != 0) {
            val lastMessage = recyclerView.get(messages-1)
            speech.speak(lastMessage.toString())
        }
        //micButton.setImageResource(R.drawable.ic_mic_off)
        //editText.setText(result)
*/

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





