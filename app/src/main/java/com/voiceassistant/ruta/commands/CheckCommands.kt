package com.voiceassistant.ruta.commands

import android.content.Context
import com.voiceassistant.ruta.ChatGptViewModel
import com.voiceassistant.ruta.Speech
import com.voiceassistant.ruta.model.Message

class CheckCommands {

    private lateinit var speech: Speech
    private lateinit var chatGptViewModel: ChatGptViewModel

    fun commands(command:String, context: Context){
        speech = Speech(context)
        when(command){
            "Слава Україні"->{
                speech.speak("Героям Слава")
                chatGptViewModel.addToChat("Героям Слава", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
            }
            else ->{
                chatGptViewModel.callApi(command,speech)
            }
        }
    }
}