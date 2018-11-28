package com.kevgilles.dm.initiativetracker.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Fighter

class FightAdapter(private val fighterList: ArrayList<Fighter>, private val lifeClicked: View.OnClickListener): RecyclerView.Adapter<FightAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_fight)!!
        val fighterName = itemView.findViewById<TextView>(R.id.tvFighterName)!!
        val rolledInit = itemView.findViewById<TextView>(R.id.tvRolledInit)!!
        val fighterHp = itemView.findViewById<TextView>(R.id.tvFighterHp)!!
        val fighterCurrentHp = itemView.findViewById<TextView>(R.id.tvCurrentHp)!!
        val fighterAc = itemView.findViewById<TextView>(R.id.tvFighterAc)!!
        val healthLayout = itemView.findViewById<LinearLayout>(R.id.healthLayout)!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fighter = fighterList[position]
        holder.fighterName.text = fighter.name
        holder.rolledInit.text = fighter.rolledInitiative.toString()
        holder.fighterHp.text = fighter.hitPoints.toString()
        holder.fighterAc.text = fighter.armorClass.toString() + "]"
        holder.fighterCurrentHp.text = fighter.currentHp.toString()
        holder.cardView.tag = position
        if (position == 0) {
//            holder.cardView.setBackgroundColor(Color.parseColor("#80cbc4"))
            holder.cardView.setCardBackgroundColor(Color.parseColor("#b2dfdb"))
        }
        holder.healthLayout.tag = position
        holder.healthLayout.setOnClickListener(lifeClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.card_fight, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return fighterList.size
    }

    fun getFighterAtPosition(position: Int): Fighter {
        return fighterList[position]
    }

    fun removeAt(position: Int): Fighter {
        val fighter = fighterList[position]
        fighterList.removeAt(position)
        notifyItemRemoved(position)
        return fighter
    }
}