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
import androidx.lifecycle.lifecycleScope
import com.example.mstartkotlintask_hussam.Controller.Network.DateService
import com.example.mstartkotlintask_hussam.Controller.Room.AppDatabase
import com.example.mstartkotlintask_hussam.Controller.Room.EventRepository
import com.example.mstartkotlintask_hussam.model.basic.MyApplication
import com.example.mstartkotlintask_hussam.model.beans.converterResults.ConverterResults
import com.example.mstartkotlintask_hussam.model.beans.events.Event
import com.example.mstartkotlintask_hussam.R
import com.example.mstartkotlintask_hussam.View.Dialog.LoadingDialog
import com.example.mstartkotlintask_hussam.databinding.FragmentSingleEventBinding
import com.example.mstartkotlintask_hussam.model.Utilties.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SingleEventFragment : Fragment() {

    private lateinit var binding: FragmentSingleEventBinding
    private val dateFormatter = SimpleDateFormat(Constants.dateFormat, Locale.ENGLISH)
    private var selectedDate = Calendar.getInstance()
    private lateinit var eventRepository: EventRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_event, container, false)
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val eventDao = appDatabase.eventDao()
        eventRepository = EventRepository(eventDao)

        val flag = arguments?.getString("FLAG")
        val position = arguments?.getInt("POSITION")

        if (flag.equals("add", true)){
            binding.deleteBtn.visibility = View.INVISIBLE
            binding.gregorianDateValue.setText(dateFormatter.format(selectedDate.time))
            convertDate()
        } else {
            binding.deleteBtn.visibility = View.VISIBLE
            getEventByPosition(position)
        }

        binding.gregorianDateValue.setOnClickListener {
            DatePickerDialog(requireContext(), R.style.DatePickerDialogTheme, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.gregorianDateValue.setText(dateFormatter.format(selectedDate.time))
                convertDate()
            }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.deleteBtn.setOnClickListener {
            deleteEvent(position)
        }

        binding.saveBtn.setOnClickListener {
            if (checkFields()){
                if (flag.equals("add", true))
                    addEvent()
                else {
                    editEvent(position)
                }
            }
        }

        return binding.root
    }

    private fun getEventByPosition(position: Int?) {

        lifecycleScope.launch {
            val event = eventRepository.getByPosition(position!!)
            binding.eventValue.setText(event.eventName)
            binding.eventDescriptionValue.setText(event.eventDescription)
            binding.gregorianDateValue.setText(event.gregorianDate)
            binding.hijriDateValue.setText(event.hijriDate)
        }
    }

    private fun addEvent() {

        lifecycleScope.launch {
            val event = Event(
                eventName = binding.eventValue.text.toString(),
                eventDescription = binding.eventDescriptionValue.text.toString(),
                gregorianDate = binding.gregorianDateValue.text.toString(),
                hijriDate = binding.hijriDateValue.text.toString(),
                serverDatetime = getCurrentDateTimeFormatted()
            )
            eventRepository.insert(event)
            parentFragmentManager.popBackStack()

        }

    }

    private fun deleteEvent(position: Int?) {

        lifecycleScope.launch {
            val event = eventRepository.getByPosition(position!!)
            eventRepository.delete(event)
            parentFragmentManager.popBackStack()
        }

    }

    private fun editEvent(position: Int?) {

        lifecycleScope.launch {
            val updateEvent = eventRepository.getByPosition(position!!)
            updateEvent.eventName = binding.eventValue.text.toString()
            updateEvent.eventDescription = binding.eventDescriptionValue.text.toString()
            updateEvent.gregorianDate = binding.gregorianDateValue.text.toString()
            updateEvent.hijriDate = binding.hijriDateValue.text.toString()
            updateEvent.serverDatetime = getCurrentDateTimeFormatted()
            eventRepository.update(updateEvent)

            parentFragmentManager.popBackStack()
        }

    }

    private fun checkFields(): Boolean {
        var checked = true

        val eventNameRegex = "^.{3,50}$".toRegex()
        if (!binding.eventValue.text.toString().matches(eventNameRegex)){
            binding.eventValue.error = getString(R.string.the_event_name_must_be_at_minimum_3_characters_maximum_50_characters)
            checked = false
        }

        val eventDescriptionRegex = "^.{10,200}$".toRegex()
        if (!binding.eventDescriptionValue.text.toString().matches(eventDescriptionRegex)) {
            binding.eventDescriptionValue.error = getString(R.string.the_event_description_must_be_at_minimum_10_characters_maximum_200_characters)
            checked = false
        }

        val dateRegex = "^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[0-2])-(\\d{4})$".toRegex()
        if (!binding.gregorianDateValue.text.toString().matches(dateRegex)) {
            binding.gregorianDateValue.error = getString(R.string.invalid_date)
            checked = false
        }

        if (!binding.hijriDateValue.text.toString().matches(dateRegex)){
            binding.hijriDateValue.error = getString(R.string.invalid_date)
            checked = false
        }

        return checked
    }

    private fun convertDate() {

        val call: Call<ConverterResults> = MyApplication.retrofit.create(DateService::class.java).convertDate(binding.gregorianDateValue.text.toString())

        call.enqueue(object : Callback<ConverterResults> {
            override fun onResponse(call: Call<ConverterResults>, response: Response<ConverterResults>) {

                if (response.isSuccessful){
                    val converterResults: ConverterResults? = response.body()

                    binding.hijriDateValue.setText(converterResults?.data?.hijri?.date)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ConverterResults>, t: Throwable) {
                Toast.makeText(requireContext(), getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getCurrentDateTimeFormatted(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.serverDateFormatter, Locale.ENGLISH)
        return dateFormat.format(calendar.time)
    }

}