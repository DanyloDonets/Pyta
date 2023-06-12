package com.voiceassistant.ruta.commands

import android.content.Context
import com.voiceassistant.ruta.ChatGptViewModel
import com.voiceassistant.ruta.Speech
import com.voiceassistant.ruta.model.Message

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
        setTimer = SetTimer(speech,chatGptViewModel, context)
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
                val time = getNumbersFromString(command)


                speech.speak("Ставлю таймер")
                chatGptViewModel.addToChat("Ставлю таймер", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

                setTimer.setTimer(command, time[0])

            }
            command.startsWith("Постав будильник на", ignoreCase = true)->{
                val timeString = command.substringAfter("Постав будильник на").trim()

                // Parse the time using SimpleDateFormat

                    speech.speak("Будильник поставлено")
                    chatGptViewModel.addToChat("Будильник поставлено", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                    val time = getNumbersFromString(command)
                    var hour = time[0]
                    var minutes = 0
                    if (time.count() > 1){
                        minutes = time[1]

                    }
                    if(command.contains("вечора") or command.contains("вечору")){
                        hour = hour+12
                    }

                     setAlarm.setAlarm(hour,minutes)
                chatGptViewModel.addToChat("Будильник поставлено на "+hour+" "+minutes, Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())
                speech.speak("Будильник поставлено на "+hour+" "+minutes)

            }
            else ->{
                chatGptViewModel.callApi(command,speech)
            }
        }
    }

    fun getNumbersFromString(input: String): List<Int> {
        val numberRegex = "\\d+(\\.\\d+)?".toRegex()
        val matches = numberRegex.findAll(input)

        val numbers = mutableListOf<Int>()
        for (match in matches) {
            val number = match.value.toIntOrNull()
            number?.let {
                numbers.add(it)
            }
        }

        return numbers
    }



}