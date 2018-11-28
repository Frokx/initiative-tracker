package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.SwipeToDeleteCallback
import com.kevgilles.dm.initiativetracker.adapters.EncounterAdapter
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import com.kevgilles.dm.initiativetracker.dataclass.Campaign.Companion.CAMPAIGN_ID
import com.kevgilles.dm.initiativetracker.dataclass.Encounter
import com.kevgilles.dm.initiativetracker.dataclass.Encounter.Companion.ENCOUNTER_ID
import com.kevgilles.dm.initiativetracker.dataclass.Team
import com.kevgilles.dm.initiativetracker.dataclass.Team.Companion.TEAM_ID
import com.kevgilles.dm.initiativetracker.viewmodels.CampaignViewModel
import kotlinx.android.synthetic.main.activity_campaign.*
import kotlinx.android.synthetic.main.card_encounter_empty.*
import kotlinx.android.synthetic.main.card_team.*


private const val TEAM_ACTIVITY_RESULT = 1
// TODO: Change cards Elevation
// TODO: Rename Campaign

class CampaignActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mCampaignVM: CampaignViewModel
    private lateinit var mEncounterAdapter: EncounterAdapter
    private var mCampaignId: Long = -1
    private var mTeamId: Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign)

        mCampaignId = intent.getLongExtra(CAMPAIGN_ID, -1)

        mCampaignVM = ViewModelProviders.of(this).get(CampaignViewModel::class.java)
        mCampaignVM.getTeam().observe(this, Observer { updateTeam(it) })
        mCampaignVM.getEncounters().observe(this, Observer { updateEncounter(it) })
        refreshTeam()
        refreshEncounters()

        card_view_team.setOnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            intent.putExtra(Team.TEAM_ID, mTeamId)
            intent.putExtra(Campaign.CAMPAIGN_ID, mCampaignId)
            startActivityForResult(intent, TEAM_ACTIVITY_RESULT)
        }

        card_encounter_empty.setOnClickListener { startNewEncounterActivity() }
        floatingActionButton.setOnClickListener { startNewEncounterActivity() }

    }
    private fun startNewEncounterActivity() {
        val intent = Intent(this, EncounterActivity::class.java)
        intent.putExtra(Campaign.CAMPAIGN_ID, mCampaignId)
        startActivity(intent)
    }

    private fun startEncounterActivity(encounterId: Long) {
        val intent = Intent(this, EncounterActivity::class.java)
        intent.putExtra(Campaign.CAMPAIGN_ID, mCampaignId)
        intent.putExtra(Encounter.ENCOUNTER_ID, encounterId)
        startActivity(intent)
    }

    override fun onPostResume() {
        super.onPostResume()
        refreshEncounters()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TEAM_ACTIVITY_RESULT && resultCode == Activity.RESULT_OK) {
            refreshTeam()
        } else {
            refreshEncounters()
        }
    }

    private fun refreshEncounters() {
        mCampaignVM.refreshEncounters(mCampaignId)
    }

    private fun refreshTeam() {
        mCampaignVM.refreshTeam(mCampaignId)
    }

    override fun onResume() {
        super.onResume()
        refreshTeam()
    }

    private fun updateEncounter(encounterList: MutableList<Encounter>) {
        if (encounterList.isEmpty()) {
            rv_encounter.visibility = View.GONE
            emptyEncounterInclude.visibility = View.VISIBLE
            emptyEncounter.text = getString(R.string.campaign_activity_new_encounter)
        } else {
            emptyEncounterInclude.visibility = View.GONE
            rv_encounter.visibility = View.VISIBLE
            updateRecyclerView(encounterList)
        }
    }

    /**
     * Refresh the view when database is updated
     */
    private fun updateRecyclerView(encounterList: MutableList<Encounter>) {
        mEncounterAdapter = EncounterAdapter(encounterList, this)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_encounter)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mEncounterAdapter

        val swipeHandlerLeft = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as EncounterAdapter
                val removedId = adapter.removeAt(viewHolder.adapterPosition)
                mCampaignVM.deleteEncounter(removedId, mCampaignId)
            }
        }
        ItemTouchHelper(swipeHandlerLeft).attachToRecyclerView(recyclerView)
    }

    private fun updateTeam(team: Team?) {
        if (team == null) {
            tvCampaignTeamCardName.text = getString(R.string.campaign_activity_card_empty)
        } else {
            mTeamId = team.id
            tvCampaignTeamCardName.text = team.name
        }
    }

    override fun onClick(view: View) {
        if (view.tag != null && view is CardView) {
            if (mTeamId == -1L) {
                Toast.makeText(this, "Please create a Team first", Toast.LENGTH_SHORT).show()
                return
            }
            val encounterId = mEncounterAdapter.getEncounterIdAtPosition(view.tag as Int)
            startEncounterActivity(encounterId)
        }
        if (view.tag != null && view is LinearLayout) {
            val encounterId = mEncounterAdapter.getEncounterIdAtPosition(view.tag as Int)
            val intent = Intent(this, TrackingActivity::class.java)
            intent.putExtra(TEAM_ID, mTeamId)
            intent.putExtra(ENCOUNTER_ID, encounterId)
            startActivity(intent)
        }
    }
}
