package com.example.mstartkotlintask_hussam.Controller.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.mstartkotlintask_hussam.R
import com.example.mstartkotlintask_hussam.View.Fragments.SingleEventFragment
import com.example.mstartkotlintask_hussam.model.beans.events.Event
import com.example.mstartkotlintask_hussam.databinding.LayoutEventBinding

class EventsAdapter(
    private val dataList: List<Event?>,
    private val editIconClickListener: OnEditIconClickListener,
    private val deleteIconClickListener: OnDeleteIconClickListener) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: LayoutEventBinding,
        private val editIconClickListener: OnEditIconClickListener,
        private val deleteIconClickListener: OnDeleteIconClickListener

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event?, position: Int) {
            binding.event = event
            binding.executePendingBindings()

            binding.editIcon.setOnClickListener {
                editIconClickListener.onEditIconClick(position)
            }

            binding.deleteIcon.setOnClickListener {
                deleteIconClickListener.onDeleteIconClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutEventBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, editIconClickListener, deleteIconClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = dataList[position]
        holder.bind(event, position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnEditIconClickListener {
        fun onEditIconClick(position: Int)
    }

    interface OnDeleteIconClickListener {
        fun onDeleteIconClick(position: Int)
    }

}
