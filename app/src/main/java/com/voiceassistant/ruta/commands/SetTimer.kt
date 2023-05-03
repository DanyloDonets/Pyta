package com.voiceassistant.ruta.commands

import android.os.CountDownTimer
import com.voiceassistant.ruta.ChatGptViewModel
import com.voiceassistant.ruta.Speech
import com.voiceassistant.ruta.model.Message

class SetTimer(private val speech: Speech, private val chatGptViewModel: ChatGptViewModel) {

    private lateinit var timer: CountDownTimer

    fun setTimer(command: String) {
        //val timeInSeconds = command.substringAfter("Постав таймер").trim().toIntOrNull()
        val timeInSeconds = 60

        if (timeInSeconds == null) {
            speech.speak("Час не розпізнано. Повторіть ще раз.")
            chatGptViewModel.addToChat("Час не розпізнано. Повторіть ще раз.", Message.SENT_BY_BOT, chatGptViewModel.getCurrentTimestamp())
        } else {
            val timeInMillis = timeInSeconds * 1000L

            timer = object : CountDownTimer(timeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsLeft = (millisUntilFinished / 1000).toInt()
                    speech.speak("Залишилось $secondsLeft секунд")
                    chatGptViewModel.addToChat("Залишилось $secondsLeft секунд", Message.SENT_BY_BOT, chatGptViewModel.getCurrentTimestamp())
                }

                override fun onFinish() {
                    speech.speak("Таймер закінчився.")
                    chatGptViewModel.addToChat("Таймер закінчився.", Message.SENT_BY_BOT, chatGptViewModel.getCurrentTimestamp())
                }
            }

            timer.start()
            speech.speak("Таймер на $timeInSeconds секунд поставлено.")
            chatGptViewModel.addToChat("Таймер на $timeInSeconds секунд поставлено.", Message.SENT_BY_BOT, chatGptViewModel.getCurrentTimestamp())
        }
    }

    fun cancelTimer() {
        timer.cancel()
        speech.speak("Таймер скасовано.")
        chatGptViewModel.addToChat("Таймер скасовано.", Message.SENT_BY_BOT, chatGptViewModel.getCurrentTimestamp())
    }
}