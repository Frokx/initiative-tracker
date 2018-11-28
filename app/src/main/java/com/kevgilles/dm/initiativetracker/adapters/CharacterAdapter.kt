package com.kevgilles.dm.initiativetracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Character

class CharacterAdapter(private val characterList: MutableList<Character>, private val itemClickListener: View.OnClickListener):
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_view_character)!!
        val characterName = itemView.findViewById<TextView>(R.id.tvCharacterName)!!
        val characterInit = itemView.findViewById<TextView>(R.id.tvRolledInit)!!
        val characterHp = itemView.findViewById<TextView>(R.id.tvFighterHp)!!
        val characterAc = itemView.findViewById<TextView>(R.id.tvFighterAc)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.card_character, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characterList[position]
        val initModifier = character.initiativeModifier
        var initModifString = initModifier.toString()
        if (initModifier > 0) { initModifString = "+$initModifier" }
        holder.characterName.text = character.name
        holder.characterInit.text = initModifString
        holder.characterAc.text = character.armorClass.toString()
        holder.characterHp.text = character.hitPoints.toString()
        holder.cardView.tag = position
        holder.cardView.setOnClickListener(itemClickListener)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    fun getCharacterAtPosition(position: Int): Character {
        return characterList[position]
    }

    fun removeAt(position: Int): Long {
        val removedId = characterList[position].id
        characterList.removeAt(position)
        notifyItemRemoved(position)
        return removedId
    }
}