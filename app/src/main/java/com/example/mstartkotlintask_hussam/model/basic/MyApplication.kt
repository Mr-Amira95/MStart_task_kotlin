package com.example.mstartkotlintask_hussam.model.basic

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.os.ProcessCompat
import com.example.mstartkotlintask_hussam.Controller.Room.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    companion object {
        lateinit var retrofit: Retrofit
    }

    override fun onCreate() {
        super.onCreate()

        AppDatabase.getDatabase(this)

        // Initialize the Retrofit instance
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.aladhan.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

}
