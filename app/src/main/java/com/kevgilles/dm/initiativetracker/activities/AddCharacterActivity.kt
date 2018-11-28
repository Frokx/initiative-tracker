package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Team
import com.kevgilles.dm.initiativetracker.viewmodels.AddCharacterViewModel
import kotlinx.android.synthetic.main.activity_add_character.*

class AddCharacterActivity : AppCompatActivity() {
    private lateinit var mCharacterVM: AddCharacterViewModel
    private var mDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_character)

        val teamId = intent.getLongExtra(Team.TEAM_ID, -1)

        mCharacterVM = ViewModelProviders.of(this).get(AddCharacterViewModel::class.java)
        mCharacterVM.setTeamId(teamId)

        but_add_again.setOnClickListener { saveCharacter() }

        but_done.setOnClickListener {
            mDone = true
            saveCharacter()
        }
    }

    /**
     * Save current character into the database
     * TODO: Add list once rather than many Characters
     */
    private fun saveCharacter() {
        val character = Character(
            name = etCharacterName.text.toString(),
            hitPoints = Integer.parseInt(etCharacterHp.text.toString()),
            armorClass = Integer.parseInt(etCharacterAc.text.toString()),
            initiativeModifier = Integer.parseInt(etCharacterInitModifier.text.toString())
        )
        mCharacterVM.addCharacter(character)

        // Prepare new screen or get back to previous Activity ?
        if (mDone) sendResultActivity()
        else cleanInterface()
    }

    private fun cleanInterface() {
        etCharacterName.text?.clear()
        etCharacterHp.text?.clear()
        etCharacterAc.text?.clear()
        etCharacterInitModifier.text?.clear()
    }

    /**
     * ActivityForResult's answer
     */
    private fun sendResultActivity() {
        Toast.makeText(this, getString(R.string.add_campaign_success), Toast.LENGTH_SHORT).show()
        val returnIntent = Intent()
        returnIntent.putExtra(Team.TEAM_ID, mCharacterVM.mTeamId)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
