package com.example.clinic_info_branch.fragments.searching_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.Notes
import com.example.clinic_info_branch.data_base.Patient
import com.example.clinic_info_branch.data_base.TreatmentProcess


class RecTreatmentProcessAdapter(private val recViewClickListener : RecViewClickListener) : RecyclerView.Adapter<RecTreatmentProcessAdapter.ViewHolder>() {

    private lateinit var list: List<TreatmentProcess>

    interface RecViewClickListener{

        fun onClick(position: Int)
    }

    fun setList(list: List<TreatmentProcess>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtDate: TextView = itemView.findViewById(R.id.txtDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.rec_treatment_process_item,
            parent, false
        )

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {

        holder.txtDate.text = list[position].visitDate

        holder.itemView.setOnClickListener { recViewClickListener.onClick(position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}