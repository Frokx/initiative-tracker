package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import com.kevgilles.dm.initiativetracker.dataclass.Encounter
import com.kevgilles.dm.initiativetracker.viewmodels.AddCreatureViewModel
import kotlinx.android.synthetic.main.activity_add_creature.*

// TODO: Edittext as input number
class AddCreatureActivity : AppCompatActivity() {
    private lateinit var mAddCreatureVM: AddCreatureViewModel
    private var mEncounterId: Long = -1
    private var mDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_creature)

        mEncounterId = intent.getLongExtra(Encounter.ENCOUNTER_ID, -1)
        Toast.makeText(this, "encounterId = $mEncounterId", Toast.LENGTH_SHORT).show()
        mAddCreatureVM = ViewModelProviders.of(this).get(AddCreatureViewModel::class.java)

        but_add_again.setOnClickListener { prepareCreature() }
        but_done.setOnClickListener {
            mDone = true
            prepareCreature()
        }
    }

    /**
     * Prepare creature in To-Save List
     */
    private fun prepareCreature() {
        val creature = Creature(
            name = etCreatureName.text.toString(),
            hitPoints = Integer.parseInt(etCreatureHp.text.toString()),
            armorClass = Integer.parseInt(etCreatureAc.text.toString()),
            initiativeModifier = Integer.parseInt(etCreatureInit.text.toString())
        )
        mAddCreatureVM.insertCreature(creature, mEncounterId)
        Log.e("PRINTTTT", creature.toString())

        if (mDone){
            sendResultActivity()
        }else {
            cleanInterface()
        }
    }

    /**
     * Reset Interface
     */
    private fun cleanInterface() {
        etCreatureName.text?.clear()
        etCreatureHp.text?.clear()
        etCreatureAc.text?.clear()
        etCreatureInit.text?.clear()
    }

    /**
     * ActivityForResult's answer
     */
    private fun sendResultActivity() {
        val returnIntent = Intent()
        returnIntent.putExtra(Encounter.ENCOUNTER_ID, mEncounterId)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
