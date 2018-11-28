package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.SwipeToDeleteCallback
import com.kevgilles.dm.initiativetracker.adapters.CharacterAdapter
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Team
import com.kevgilles.dm.initiativetracker.fragments.TeamNameFragment
import com.kevgilles.dm.initiativetracker.fragments.TeamRenameFragment
import com.kevgilles.dm.initiativetracker.viewmodels.TeamViewModel
import kotlinx.android.synthetic.main.activity_team.*
import kotlinx.android.synthetic.main.card_character.*

private const val ADD_CHAR_REQUEST_CODE: Int = 1
private const val EDIT_CHAR_REQUEST_CODE: Int = 2

class TeamActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mTeamVM: TeamViewModel
    private lateinit var mAdapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        val teamId = intent.getLongExtra(Team.TEAM_ID, -1)
        val campaignId = intent.getLongExtra(Campaign.CAMPAIGN_ID, -1)
        mTeamVM = ViewModelProviders.of(this).get(TeamViewModel::class.java)
        mTeamVM.setIds(campaignId, teamId)
        mTeamVM.getCharacterList().observe(this, Observer { refreshRecyclerView(it!!) })
        mTeamVM.needTeamName().observe(this, Observer { requestNewTeamName(it) })
        refreshCharacters()

        floatingActionButton!!.setOnClickListener {
            val intent = Intent(this, AddCharacterActivity::class.java)
            intent.putExtra(Team.TEAM_ID, mTeamVM.getTeamId())
            startActivityForResult(intent, ADD_CHAR_REQUEST_CODE)
        }
    }

    /**
     * Menu Creation
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.team_edit, menu)
        return true
    }

    /**
     * Menu click handler
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_rename_team -> {
                val fragment = TeamRenameFragment()
                fragment.setCurrentName(tvCharacterName.text.toString())
                fragment.show(supportFragmentManager, "EditTeamNameRequest")
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun requestNewTeamName(boolean: Boolean) {
        if (boolean) {
            val fragment = TeamNameFragment()
            fragment.show(supportFragmentManager, "teamNameRequest")
        }
    }

    fun updateTeamName(newTeamName: String?) {
        if (newTeamName != null) {
            mTeamVM.renameTeam(newTeamName)
            Toast.makeText(this, "Team was Renamed $newTeamName", Toast.LENGTH_SHORT).show()
        }
    }

    fun setTeamName(teamName: String?) {
        if (teamName == null) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            mTeamVM.setTeamName(teamName)
        }
    }

    /**
     * Refresh Recycler View on Characters Added
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            refreshCharacters()
        }
    }

    /**
     * Convenient function to call a refresh on the RV post edition
     */
    private fun refreshCharacters() {
        mTeamVM.refreshCharacterList()
    }

    /**
     * Bring any team changes to the view
     */
    private fun refreshRecyclerView(characterList: MutableList<Character>) {
        if (characterList.isEmpty()) {
            tvNoCharTips.visibility = View.VISIBLE
        } else {
            tvNoCharTips.visibility = View.GONE
        }

        mAdapter = CharacterAdapter(characterList, this)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_players)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        val swipeHandlerLeft = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as CharacterAdapter
                val removedId = adapter.removeAt(viewHolder.adapterPosition)
                mTeamVM.deleteCharacter(removedId)
            }
        }
        ItemTouchHelper(swipeHandlerLeft).attachToRecyclerView(recyclerView)
    }

    /**
     * When you click one of the RV elements, enter edition mode for this character
     */
    override fun onClick(view: View) {
        if (view.tag != null) {
            val selectedCharacter = mAdapter.getCharacterAtPosition(view.tag as Int)
            val intent = Intent(this, EditCharacterActivity::class.java)
            intent.putExtra("character", selectedCharacter)
            startActivityForResult(intent, EDIT_CHAR_REQUEST_CODE)
        }
    }
}
