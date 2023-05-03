package com.voiceassistant.ruta.commands

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import com.voiceassistant.ruta.ChatGptViewModel
import com.voiceassistant.ruta.Speech
import com.voiceassistant.ruta.model.Message

class Call(private val context: Context) {

    fun callContact(contactName: String, speech: Speech, chatGptViewModel: ChatGptViewModel) {
        val contactUri = Uri.withAppendedPath(
            ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
            Uri.encode(contactName)
        )
        val cursor = context.contentResolver.query(
            contactUri,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                val phoneNumberColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (phoneNumberColumnIndex >= 0) {
                    val phoneNumber = it.getString(phoneNumberColumnIndex)
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:$phoneNumber")
                    if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                        context.startActivity(callIntent)
                    } else {
                        speech.speak("Номер контакту "+contactName+" не дійсний")
                        chatGptViewModel.addToChat("Номер контакту "+contactName+" не дійсний", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

                    }
                } else {
                    speech.speak("Номер контакту "+contactName+" не знайдено")
                    chatGptViewModel.addToChat("Номер контакту "+contactName+" не знайдено", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

                }
            } else {
                speech.speak("Контакт "+contactName+" не знайдено")
                chatGptViewModel.addToChat("Контакт "+contactName+" не знайдено", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

            }
        }
    }
}