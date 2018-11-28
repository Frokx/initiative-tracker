package com.kevgilles.dm.initiativetracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Campaign

class CampaignAdapter(private val campaignList: List<Campaign>, private val itemClickListener: View.OnClickListener)
    : RecyclerView.Adapter<CampaignAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_view)!!
        val tvCampaignName = itemView.findViewById<TextView>(R.id.tv_campaign_name)!!
        val tvCampaignGame = itemView.findViewById<TextView>(R.id.tv_campaign_game)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.card_campaign, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val campaign = campaignList[position]
        holder.tvCampaignName.text = campaign.name
        holder.tvCampaignGame.text = campaign.game
        holder.cardView.tag = position
        holder.cardView.setOnClickListener(itemClickListener)
    }

    override fun getItemCount(): Int {
        return campaignList.size
    }

    fun getCampaignIdAtPosition(position: Int): Long {
        return campaignList[position].id
    }
}