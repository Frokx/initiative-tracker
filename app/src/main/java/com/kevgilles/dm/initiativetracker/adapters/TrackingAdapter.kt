package com.kevgilles.dm.initiativetracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import com.kevgilles.dm.initiativetracker.dataclass.Fighter

class TrackingAdapter(private val trackingList: List<Fighter>) :
    RecyclerView.Adapter<TrackingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_trackable)!!
        val trackingName = itemView.findViewById<TextView>(R.id.tvFighterName)!!
        val trackingAc = itemView.findViewById<TextView>(R.id.tvFighterAc)!!
        val trackingHp = itemView.findViewById<TextView>(R.id.tvFighterHp)!!
        val trackingInit = itemView.findViewById<TextView>(R.id.tvRolledInit)!!
    }

    override fun onBindViewHolder(holder: TrackingAdapter.ViewHolder, position: Int) {
        val tracked = trackingList[position]
        val current: Fighter = when (tracked) {
            is Creature -> Creature(tracked)
            else -> Character(tracked as Character)
        }
        holder.trackingName.text = current.name
        holder.trackingAc.text = current.armorClass.toString()
        holder.trackingHp.text = current.hitPoints.toString()
        holder.trackingInit.text = current.initiativeModifier.toString()
        holder.cardView.tag = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.card_trackable, parent, false)
        return TrackingAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return trackingList.size
    }
}
