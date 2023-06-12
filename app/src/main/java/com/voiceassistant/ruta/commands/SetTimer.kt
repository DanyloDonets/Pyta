package com.voiceassistant.ruta.commands

import android.content.Context
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import com.voiceassistant.ruta.ChatGptViewModel
import com.voiceassistant.ruta.Speech
import com.voiceassistant.ruta.model.Message

class SetTimer(private val speech: Speech, private val chatGptViewModel: ChatGptViewModel, private val context: Context) {

    private lateinit var timer: CountDownTimer

    fun setTimer(command: String, time : Int) {
        //val timeInSeconds = command.substringAfter("Постав таймер").trim().toIntOrNull()
        val timeInSeconds = time

        if (timeInSeconds == null) {
            speech.speak("Час не розпізнано. Повторіть ще раз.")
            chatGptViewModel.addToChat("Час не розпізнано. Повторіть ще раз.", Message.SENT_BY_BOT, chatGptViewModel.getCurrentTimestamp())
        } else {
            val timeInMillis = timeInSeconds * 1000L

            timer = object : CountDownTimer(timeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsLeft = (millisUntilFinished / 1000).toInt()

                }

                override fun onFinish() {
                    vibrate(context,5000)
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

    fun vibrate(context: Context, milliseconds: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (vibrator.hasVibrator()) {
            val vibrationEffect = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }


}