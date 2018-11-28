package com.kevgilles.dm.initiativetracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.adapters.TrackingAdapter
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Encounter
import com.kevgilles.dm.initiativetracker.dataclass.Fighter
import com.kevgilles.dm.initiativetracker.dataclass.Team
import com.kevgilles.dm.initiativetracker.fragments.CharactersRollsFragment
import com.kevgilles.dm.initiativetracker.viewmodels.TrackingViewModel
import kotlinx.android.synthetic.main.activity_tracking.*


class TrackingActivity : AppCompatActivity() {

    private lateinit var mTrackingVM: TrackingViewModel
    private lateinit var mAdapter: TrackingAdapter
    private var mEncounterId: Long = -1
    private var mTeamId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        mEncounterId = intent.getLongExtra(Encounter.ENCOUNTER_ID, -1)
        mTeamId = intent.getLongExtra(Team.TEAM_ID, -1)

        mTrackingVM = ViewModelProviders.of(this).get(TrackingViewModel::class.java)
        mTrackingVM.getEntries().observe(this, Observer { updateRV(it) })
        mTrackingVM.prepareFight(teamId = mTeamId, encounterId = mEncounterId)

        mTrackingVM.needCharactersRolls().observe(this, Observer { if (it) charactersRollsInterface() })
        mTrackingVM.isFightReady().observe(this, Observer { if (it) startFightTracker() })

        floatingActionButton.setOnClickListener { mTrackingVM.rollInitiative() }
    }

    /**
     * When VM says fight is ready we create an intent and give it the fighters'list
     */
    private fun startFightTracker() {
        val intent = Intent(this, FightActivity::class.java)
        intent.putParcelableArrayListExtra("fighters", ArrayList(mTrackingVM.getFightersList()))
        startActivity(intent)
    }

    /**
     * If VM requests roll for character initiative we show the needed fragmentDialog
     */
    private fun charactersRollsInterface() {
        val fragment = CharactersRollsFragment()
        val args = Bundle()
        args.putParcelableArrayList("characters", ArrayList(mTrackingVM.getCharactersRoll()))
        fragment.arguments = args
        fragment.show(supportFragmentManager, "ok TAG")
    }

    /**
     * Every time something changes, the VM will request a rv adapter refresh
     */
    private fun updateRV(entryList: List<Fighter>) {
        mAdapter = TrackingAdapter(entryList)
        val recyclerView = findViewById<RecyclerView>(R.id.rvTracking)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }

    fun sendCharacterRollToVM(characters: ArrayList<Character>) {
        mTrackingVM.inputCharRolls(characters)
    }
}
