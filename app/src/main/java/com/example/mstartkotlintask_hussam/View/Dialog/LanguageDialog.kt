import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.fragment.app.DialogFragment
import com.example.mstartkotlintask_hussam.R
import com.example.mstartkotlintask_hussam.model.Utilties.Constants
import com.example.mstartkotlintask_hussam.model.basic.MyApplication
import java.util.*

class LanguageDialog : DialogFragment() {

    private lateinit var languageRadioGroup: RadioGroup
    private lateinit var arabic: RadioButton
    private lateinit var english: RadioButton
    private lateinit var btnApply: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_language, null)
        setupViews(view)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(view)
        return alertDialogBuilder.create()
    }

    private fun setupViews(view: View) {
        languageRadioGroup = view.findViewById(R.id.languages_options)
        btnApply = view.findViewById(R.id.apply_button)
        arabic = view.findViewById(R.id.arabic)
        english = view.findViewById(R.id.english)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val selectedLanguage = sharedPref?.getString(Constants.language, "en")

        if (selectedLanguage == "ar") {
            arabic.isChecked = true
        } else {
            english.isChecked = true
        }

        btnApply.setOnClickListener { applyLanguageSelection() }
    }

    private fun applyLanguageSelection() {

        val selectedLanguage = when (languageRadioGroup.checkedRadioButtonId) {
            R.id.arabic -> "ar"
            else -> "en"
        }

        saveSelectedLanguage(selectedLanguage)

        (requireActivity().application as MyApplication).restartApp(requireContext())

        dismiss()
    }

    private fun saveSelectedLanguage(language: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString(Constants.language, language)
        editor?.apply()
    }

}