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


class RecPatientAdapter(private val recViewClickListener : RecViewClickListener) : RecyclerView.Adapter<RecPatientAdapter.ViewHolder>() {

    private lateinit var list: List<Patient>


    interface RecViewClickListener{
        fun delete(position: Int)
        fun onClick(position: Int)
    }

    fun setList(list: List<Patient>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtPhone: TextView = itemView.findViewById(R.id.txtPhone)
        val txtPlace: TextView = itemView.findViewById(R.id.txtPlace)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.rec_patient_item,
            parent, false
        )


        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {

        holder.txtName.text = list[position].patientName
        holder.txtPhone.text = list[position].phone
        holder.txtPlace.text = list[position].placeOfResidence

        holder.btnDelete.setOnClickListener { recViewClickListener.delete(position) }

        holder.itemView.setOnClickListener { recViewClickListener.onClick(position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}