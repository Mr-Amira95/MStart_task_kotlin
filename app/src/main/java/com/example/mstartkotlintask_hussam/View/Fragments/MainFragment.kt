package com.example.mstartkotlintask_hussam.View.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mstartkotlintask_hussam.Controller.Network.DateService
import com.example.mstartkotlintask_hussam.model.basic.MyApplication
import com.example.mstartkotlintask_hussam.model.beans.converterResults.ConverterResults
import com.example.mstartkotlintask_hussam.R
import com.example.mstartkotlintask_hussam.View.Dialog.LoadingDialog
import com.example.mstartkotlintask_hussam.databinding.FragmentMainBinding
import com.example.mstartkotlintask_hussam.model.Utilties.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val dateFormatter = SimpleDateFormat(Constants.dateFormat, Locale.ENGLISH)
    private var currentDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.gregorianDate.setText(dateFormatter.format(currentDate.time))

        clicks()

        return binding.root
    }

    private fun clicks(){
        binding.eventsBtn.setOnClickListener {
            setFragment(EventsFragment())
        }

        binding.gregorianDate.setOnClickListener {
            DatePickerDialog(requireContext(), R.style.DatePickerDialogTheme, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                currentDate.set(Calendar.YEAR, year)
                currentDate.set(Calendar.MONTH, month)
                currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.gregorianDate.setText(dateFormatter.format(currentDate.time))
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.convertBtn.setOnClickListener {
            showLoadingDialog(parentFragmentManager)
            convertDate()
        }
    }

    private fun setFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun convertDate() {
        val call: Call<ConverterResults> = MyApplication.retrofit.create(DateService::class.java).convertDate(binding.gregorianDate.text.toString())

        call.enqueue(object : Callback<ConverterResults> {
            override fun onResponse(call: Call<ConverterResults>, response: Response<ConverterResults>) {

                if (response.isSuccessful){
                    val converterResults: ConverterResults? = response.body()

                    binding.hijriTitle.visibility = View.VISIBLE
                    binding.hijriValue.visibility = View.VISIBLE
                    binding.hijriValue.text = converterResults?.data?.hijri?.date
                } else {
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                }

                hideLoadingDialog(parentFragmentManager)

            }

            override fun onFailure(call: Call<ConverterResults>, t: Throwable) {
                hideLoadingDialog(parentFragmentManager)
                Toast.makeText(requireContext(), getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoadingDialog(fragmentManager: FragmentManager) {
        val loadingDialog = LoadingDialog()
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.add(loadingDialog, "LoadingDialog")
        transaction.commitAllowingStateLoss()
    }

    private fun hideLoadingDialog(fragmentManager: FragmentManager) {
        val loadingDialog = fragmentManager.findFragmentByTag("LoadingDialog")
        if (loadingDialog is DialogFragment) {
            loadingDialog.dismissAllowingStateLoss()
        }
    }

}

