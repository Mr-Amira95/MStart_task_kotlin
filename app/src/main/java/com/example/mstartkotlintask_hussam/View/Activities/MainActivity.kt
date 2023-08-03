package com.example.mstartkotlintask_hussam.View.Activities

import LanguageDialog
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mstartkotlintask_hussam.R
import com.example.mstartkotlintask_hussam.View.Fragments.MainFragment
import com.example.mstartkotlintask_hussam.databinding.ActivityMainBinding
import com.example.mstartkotlintask_hussam.model.Utilties.Constants
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLocale()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.language.setOnClickListener {
            val languageDialog = LanguageDialog()
            languageDialog.show(supportFragmentManager, "language_dialog")
        }

        if (savedInstanceState == null) {
            val mainFragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, mainFragment)
                .commit()
        }
    }

    private fun setLocale() {
        val locale = Locale(getPreferences(Context.MODE_PRIVATE).getString(Constants.language, "en")!!)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.setLocale(locale)

        val resources = baseContext.resources
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}