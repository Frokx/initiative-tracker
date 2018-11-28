package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import com.kevgilles.dm.initiativetracker.viewmodels.EditCreatureViewModel
import kotlinx.android.synthetic.main.activity_edit_creature.*

class EditCreatureActivity : AppCompatActivity() {

    private lateinit var mCreature: Creature
    private lateinit var mEditCreatureVM: EditCreatureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_creature)

        mCreature = intent.getParcelableExtra("creature")
        prepareView()

        mEditCreatureVM = ViewModelProviders.of(this).get(EditCreatureViewModel::class.java)

        but_done.setOnClickListener { emptyFieldsCheck() }
    }

    private fun emptyFieldsCheck() {
        val creatureName = etCreatureName.text.toString()
        val creatureAc = etCreatureAc.text.toString()
        val creatureHp = etCreatureHp.text.toString()
        val creatureInit = etCreatureInitModifier.text.toString()
        if (creatureName != "" && creatureAc != "" && creatureHp != "" && creatureInit != "") {
            val newCreature = Creature(
                name = creatureName,
                armorClass = Integer.parseInt(creatureAc),
                hitPoints = Integer.parseInt(creatureHp),
                initiativeModifier = Integer.parseInt(creatureInit)
            )
            mEditCreatureVM.updateCreature(mCreature, newCreature)
            sendResultActivity()
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * ActivityForResult's answer
     */
    private fun sendResultActivity() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun prepareView() {
        etCreatureName.setText(mCreature.name)
        etCreatureAc.setText(mCreature.armorClass.toString())
        etCreatureHp.setText(mCreature.hitPoints.toString())
        etCreatureInitModifier.setText(mCreature.initiativeModifier.toString())
    }
}