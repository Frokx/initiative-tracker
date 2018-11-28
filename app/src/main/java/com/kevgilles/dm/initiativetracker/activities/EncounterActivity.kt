package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.SwipeToDeleteCallback
import com.kevgilles.dm.initiativetracker.adapters.CreatureAdapter
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import com.kevgilles.dm.initiativetracker.dataclass.Encounter
import com.kevgilles.dm.initiativetracker.fragments.EncounterNameFragment
import com.kevgilles.dm.initiativetracker.fragments.EncounterRenameFragment
import com.kevgilles.dm.initiativetracker.viewmodels.EncounterViewModel
import kotlinx.android.synthetic.main.activity_encounter.*

// TODO: When Encounter is empty, go to Add Creature

private const val ADD_CREATURE_RESULT = 1
private const val EDIT_CREATURE_RESULT = 2

class EncounterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAdapter: CreatureAdapter

    private lateinit var mEncounterVM: EncounterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encounter)

        val campaignId = intent.getLongExtra(Campaign.CAMPAIGN_ID, -1)
        val encounterId = intent.getLongExtra(Encounter.ENCOUNTER_ID, -1)

        mEncounterVM = ViewModelProviders.of(this).get(EncounterViewModel::class.java)
        mEncounterVM.setIds(encounterId, campaignId)
        mEncounterVM.getCreatureList().observe(this, Observer { updateRecycler(it) })
        mEncounterVM.needEncounterName().observe(this, Observer { requestNewEncounterName(it) })
        refreshCreatures()

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddCreatureActivity::class.java)
            intent.putExtra(Encounter.ENCOUNTER_ID, mEncounterVM.getEncounterId())
            startActivityForResult(intent, ADD_CREATURE_RESULT)
        }
    }

    /**
     * Menu Creation
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.encounter_menu, menu)
        return true
    }

    /**
     * Menu click handler
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_rename_encounter -> {
                val fragment = EncounterRenameFragment()
                fragment.show(supportFragmentManager, "EditEncounterNameRequest")
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun requestNewEncounterName(boolean: Boolean) {
        if (boolean) {
            val fragment = EncounterNameFragment()
            fragment.show(supportFragmentManager, "encounterNameRequest")
        }
    }

    /**
     * Refresh Recycler View on creature Added
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            refreshCreatures()
        }
    }

    fun setEncounterName(teamName: String?) {
        if (teamName == null) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            mEncounterVM.setEncounterName(teamName)
        }
    }

    fun updateEncounterName(newName: String?) {
        if (newName != null) {
            mEncounterVM.renameEncounter(newName)
            Toast.makeText(this, "This encounter was renamed: $newName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshCreatures(){
        if (mEncounterVM.getEncounterId() != -1L) {
            mEncounterVM.refreshCreatures()
        }
    }

    private fun updateRecycler(creatureList: MutableList<Creature>) {
        if (creatureList.isEmpty()) {
            tvNoCreatureTips.visibility = View.VISIBLE
        } else {
            tvNoCreatureTips.visibility = View.GONE
        }
        mAdapter = CreatureAdapter(creatureList, this)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_creature)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        val swipeHandlerLeft = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as CreatureAdapter
                val removedId = adapter.removeAt(viewHolder.adapterPosition)
                mEncounterVM.deleteCreature(removedId)
            }
        }
        ItemTouchHelper(swipeHandlerLeft).attachToRecyclerView(recyclerView)
    }

    override fun onClick(view: View) {
        if (view.tag != null) {
            val selectedCreature = mAdapter.getCreatureAtPosition(view.tag as Int)
            val intent = Intent(this, EditCreatureActivity::class.java)
            intent.putExtra("creature", selectedCreature)
            startActivityForResult(intent, EDIT_CREATURE_RESULT)
        }
    }
}
