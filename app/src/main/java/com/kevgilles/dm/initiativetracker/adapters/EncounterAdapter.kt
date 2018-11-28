package com.kevgilles.dm.initiativetracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Encounter

class EncounterAdapter(private val encounterList: MutableList<Encounter>, private val onClickListener: View.OnClickListener): RecyclerView.Adapter<EncounterAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_view_encounter)!!
        val tvEncounterName = itemView.findViewById<TextView>(R.id.tvCampaignEncounterCardName)!!
        val imageViewStart = itemView.findViewById<LinearLayout>(R.id.start_tracking_image)!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val encounter = encounterList[position]
        holder.tvEncounterName.text = encounter.name
        holder.imageViewStart.tag = position
        holder.cardView.tag = position
        holder.imageViewStart.setOnClickListener(onClickListener)
        holder.cardView.setOnClickListener(onClickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.card_encounter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return encounterList.size
    }

    fun getEncounterIdAtPosition(position: Int): Long {
        return encounterList[position].id
    }

    fun removeAt(position: Int): Long {
        val removedId = encounterList[position].id
        encounterList.removeAt(position)
        notifyItemRemoved(position)
        return removedId
    }
}
