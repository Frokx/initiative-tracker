package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import com.kevgilles.dm.initiativetracker.viewmodels.AddCampaignState
import com.kevgilles.dm.initiativetracker.viewmodels.AddCampaignStateFailure
import com.kevgilles.dm.initiativetracker.viewmodels.AddCampaignStateSuccess
import com.kevgilles.dm.initiativetracker.viewmodels.AddCampaignViewModel
import kotlinx.android.synthetic.main.activity_add_campaign.*

class AddCampaignActivity : AppCompatActivity() {
    private lateinit var addCampaignViewModel: AddCampaignViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_campaign)

        addCampaignViewModel = ViewModelProviders.of(this).get(AddCampaignViewModel::class.java)
        addCampaignViewModel.getState().observe(this, Observer { displayResult(it!!) })

        // Button Listener
        but_create_campaign.setOnClickListener{
            val name: String = tv_campaign_name.text.toString()
            val game: String = tv_game_name.text.toString()

            if (name != "" && game != "")
                addCampaignViewModel.addCampaign(name = name, game = game)
            else
                Toast.makeText(this, getString(R.string.add_campaign_fill_the_blanks), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun displayResult(state: AddCampaignState){
        when(state){
            is AddCampaignStateSuccess -> {
                val returnIntent = Intent()
                returnIntent.putExtra(Campaign.CAMPAIGN_ID, state.campaignId)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            AddCampaignStateFailure -> Toast.makeText(this, getString(R.string.add_campaign_failure), Toast.LENGTH_SHORT).show()
        }
    }
}
