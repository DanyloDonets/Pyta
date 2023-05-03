package com.voiceassistant.ruta.commands

import android.content.Context
import com.voiceassistant.ruta.ChatGptViewModel
import com.voiceassistant.ruta.Speech
import com.voiceassistant.ruta.model.Message
import java.text.SimpleDateFormat
import java.util.*

class CheckCommands() {


    private lateinit var  openWebsite: OpenWebsite
    private  lateinit var openApps: OpenApps
    private  lateinit var callContact : Call
    private lateinit var  setAlarm: SetAlarm
    private lateinit var setTimer: SetTimer
    fun commands(command:String,chatGptViewModel: ChatGptViewModel, context: Context, speech: Speech){
        openWebsite = OpenWebsite(context)
        setAlarm = SetAlarm(context)
        callContact = Call(context)
        setTimer = SetTimer(speech,chatGptViewModel)
        openApps = OpenApps(context)
        when{
            command.startsWith("Слава Україні", ignoreCase = true)->{
                chatGptViewModel.addToChat("Героям Слава", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                speech.speak("Героям Слава")

            }
            command.startsWith("Відкрий сайт", ignoreCase = true) ->{

                val website = command.substringAfter("Відкрий сайт", "").trim()
                speech.speak("Відкриваю сайт"+ website)
                chatGptViewModel.addToChat("Відкриваю сайт"+ website, Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                openWebsite.openWebsite(website)
            }
            command.startsWith("Відкрий сторінку", ignoreCase = true) ->{

                val website = command.substringAfter("Відкрий сторінку", "").trim()
                speech.speak("Відкриваю сторінку"+ website)
                chatGptViewModel.addToChat("Відкриваю сторінку"+ website, Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                openWebsite.openWebsite(website)
            }
            command.startsWith("Відкрий", ignoreCase = true) -> {
                // Extract the app name from the command
                val com = command.capitalize()
                val appName = com.substringAfter("Відкрий", "").trim()
                speech.speak("Відкриваю "+appName)
                chatGptViewModel.addToChat("Відкриваю "+appName, Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                // Launch the app
                openApps.startApp(appName, speech, chatGptViewModel)
            }
            command.startsWith("Зателефонуй", ignoreCase = true)  ->{
                val contactName = command.substringAfter("Зателефонуй", "").trim()
                speech.speak("Телефоную "+contactName)
                chatGptViewModel.addToChat("Телефоную  "+contactName, Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

                callContact.callContact(contactName, speech, chatGptViewModel)
            }
            command.startsWith("Подзвони", ignoreCase = true) ->{
                val contactName = command.substringAfter("Подзвони", "").trim()
                speech.speak("Телефоную "+contactName)
                chatGptViewModel.addToChat("Телефоную "+contactName, Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

                callContact.callContact(contactName, speech, chatGptViewModel)
            }
            command.startsWith("Постав таймер", ignoreCase = true) ->{


                speech.speak("Ставлю таймер")
                chatGptViewModel.addToChat("Ставлю таймер", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

                setTimer.setTimer(command)
            }
            command.startsWith("Постав будильник на", ignoreCase = true)->{
                val timeString = command.substringAfter("Постав будильник на").trim()

                // Parse the time using SimpleDateFormat
                val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val date = sdf.parse(timeString)

                // If the time is in the past, add one day to the date
                if (date.before(Date())) {
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    //setAlarm.setAlarm(calendar.timeInMillis)
                    speech.speak("Будильник поставлено")
                    chatGptViewModel.addToChat("Будильник поставлено", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                } else {
                    speech.speak("Будильник поставлено")
                    chatGptViewModel.addToChat("Будильник поставлено", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                    setAlarm.setAlarm()
                }
            }
            else ->{
                chatGptViewModel.callApi(command,speech)
            }
        }
    }
}