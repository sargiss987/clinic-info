package com.example.clinic_info_branch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.models.Notes

class RecNoteAdapter(private val recViewClickListener: RecViewClickListener) :
    RecyclerView.Adapter<RecNoteAdapter.ViewHolder>() {

    private lateinit var list: List<Notes>

    interface RecViewClickListener {
        fun dialing(position: Int)
        fun delete(position: Int)
    }

    fun setList(list: List<Notes>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtPhone: TextView = itemView.findViewById(R.id.txtPhone)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val btnCall: ImageView = itemView.findViewById(R.id.btnCall)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.rec_note_item,
            parent, false
        )

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtName.text = list[position].name
        holder.txtPhone.text = list[position].phone
        holder.txtDate.text = list[position].date
        holder.txtTime.text = list[position].time

        holder.btnCall.setOnClickListener { recViewClickListener.dialing(position) }
        holder.btnDelete.setOnClickListener { recViewClickListener.delete(position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}