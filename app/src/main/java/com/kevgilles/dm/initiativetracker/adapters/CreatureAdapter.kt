package com.kevgilles.dm.initiativetracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Creature

class CreatureAdapter(private val creatureList: MutableList<Creature>, private val onClickListener: View.OnClickListener):
    RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_creature)!!
        val enemyName = itemView.findViewById<TextView>(R.id.tvCreatureName)!!
        val enemyAc = itemView.findViewById<TextView>(R.id.tvCreatureAc)!!
        val enemyHp = itemView.findViewById<TextView>(R.id.tvCreatureHp)!!
        val enemyInit = itemView.findViewById<TextView>(R.id.tvCreatureInit)!!
    }

    override fun onBindViewHolder(holder: CreatureAdapter.ViewHolder, position: Int) {
        val enemy = creatureList[position]
        holder.enemyName.text = enemy.name
        holder.enemyAc.text = enemy.armorClass.toString()
        holder.enemyHp.text = enemy.hitPoints.toString()
        holder.enemyInit.text = enemy.initiativeModifier.toString()
        holder.cardView.tag = position
        holder.cardView.setOnClickListener(onClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.card_creature, parent, false)
        return CreatureAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return creatureList.size
    }

    fun getCreatureAtPosition(position: Int): Creature {
        return creatureList[position]
    }

    fun removeAt(position: Int): Long {
        val removedId = creatureList[position].id
        creatureList.removeAt(position)
        notifyItemRemoved(position)
        return removedId
    }
}