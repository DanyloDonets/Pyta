package com.voiceassistant.ruta.commands

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import com.voiceassistant.ruta.ChatGptViewModel
import com.voiceassistant.ruta.Speech
import com.voiceassistant.ruta.model.Message

class OpenApps (private val context: Context){
    fun startApp(appName: String, speech: Speech, chatGptViewModel: ChatGptViewModel) {
        // Convert the app name to package name
        val packageManager = context.packageManager
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        var packageName: String? = null

        for (app in apps) {
            if (app.loadLabel(packageManager).toString().equals(appName, ignoreCase = true)) {
                packageName = app.packageName
                break
            }
        }

        if (packageName == null) {
            // App is not installed, show error message
            speech.speak("Я не змогла знайти додаток "+appName +" на вашому пристрої")
            chatGptViewModel.addToChat("Я не змогла знайти додаток "+appName +" на вашому пристрої", Message.SENT_BY_BOT,chatGptViewModel.getCurrentTimestamp())

            Toast.makeText(context, "App $appName is not installed on your device", Toast.LENGTH_SHORT).show()
        } else {
            // App is installed, so start it
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            context.startActivity(intent)
        }
    }

}