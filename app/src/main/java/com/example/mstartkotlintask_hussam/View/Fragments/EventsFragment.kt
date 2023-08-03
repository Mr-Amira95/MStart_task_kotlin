package com.example.mstartkotlintask_hussam.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mstartkotlintask_hussam.Controller.Adapters.EventsAdapter
import com.example.mstartkotlintask_hussam.Controller.Room.AppDatabase
import com.example.mstartkotlintask_hussam.Controller.Room.EventRepository
import com.example.mstartkotlintask_hussam.model.beans.events.Event
import com.example.mstartkotlintask_hussam.R
import com.example.mstartkotlintask_hussam.View.Dialog.LoadingDialog
import com.example.mstartkotlintask_hussam.databinding.FragmentEventsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsFragment : Fragment(), EventsAdapter.OnEditIconClickListener, EventsAdapter.OnDeleteIconClickListener {

    private lateinit var binding: FragmentEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events, container, false)

        getEvents()
        clicks()

        return binding.root
    }

    private fun clicks() {
        binding.addNew.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("FLAG", "add")

            setFragment(SingleEventFragment(), bundle)
        }
    }

    private fun setFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getData(callback: (List<Event>) -> Unit) {

        val eventRepository = EventRepository(AppDatabase.getDatabase(requireContext()).eventDao())

        lifecycleScope.launch {
            callback(eventRepository.getAll())
        }
    }

    private fun getEvents() {

        getData { events ->
            if (events.isNotEmpty()) {
                val eventsAdapter = EventsAdapter(events, this, this)
                binding.eventsRecyclerview.adapter = eventsAdapter
                binding.eventsRecyclerview.layoutManager = LinearLayoutManager(context)
                binding.noResults.visibility = View.GONE
            } else {
                binding.noResults.visibility = View.VISIBLE
                binding.eventsRecyclerview.visibility = View.GONE
            }
        }
    }

    override fun onEditIconClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("FLAG", "edit")
        bundle.putInt("POSITION", position)

        val singleEventFragment = SingleEventFragment()
        singleEventFragment.arguments = bundle

        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, singleEventFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDeleteIconClick(position: Int) {

        val eventRepository = EventRepository(AppDatabase.getDatabase(requireContext()).eventDao())

        lifecycleScope.launch {
            val event = eventRepository.getByPosition(position)
            eventRepository.delete(event)
            getEvents()
        }
    }

}