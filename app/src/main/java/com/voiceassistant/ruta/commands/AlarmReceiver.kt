package com.voiceassistant.ruta.commands

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import com.voiceassistant.ruta.R

class AlarmReceiver {
    class AlarmReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)
            // Обробка події спрацьованого будильника
            Toast.makeText(context, "Будильник завершено", Toast.LENGTH_SHORT).show()

            // Виклик методів для вібрації та відтворення звуку
            //val SetAlarm = SetAlarm(context)
            //SetAlarm.vibrate()
            //SetAlarm.playAlarmSound()
            mediaPlayer.start()
        }
    }
}