package  com.voiceassistant.ruta.commands

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import java.util.*

class SetAlarm(private val context: Context) {

    private lateinit var alarmManager: AlarmManager
    private lateinit var vibrator: Vibrator
    private lateinit var ringtone: Ringtone

    fun setAlarm(hours: Int, minutes: Int) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).let {
            RingtoneManager.getRingtone(context, it)
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            getAlarmPendingIntent()
        )
    }

    private fun getAlarmPendingIntent(): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun vibrate() {
        val vibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(vibrationEffect)
    }

     fun playAlarmSound() {
        ringtone.play()
    }

     fun stopAlarmSound() {
        ringtone.stop()
    }

    companion object {
        private const val ALARM_REQUEST_CODE = 123
    }
}