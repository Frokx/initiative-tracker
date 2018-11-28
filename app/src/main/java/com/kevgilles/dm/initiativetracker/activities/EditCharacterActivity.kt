package com.kevgilles.dm.initiativetracker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.viewmodels.EditCharacterViewModel
import kotlinx.android.synthetic.main.activity_edit_character.*

class EditCharacterActivity : AppCompatActivity() {
    private lateinit var mCharacter: Character
    private lateinit var mEditCharVM: EditCharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_character)

        mCharacter = intent.getParcelableExtra("character")
        prepareView()

        mEditCharVM = ViewModelProviders.of(this).get(EditCharacterViewModel::class.java)

        but_done.setOnClickListener{emptyFieldsCheck()}
    }

    private fun emptyFieldsCheck() {
        val charName = etCharacterName.text.toString()
        val charAc = etCharacterAc.text.toString()
        val charHp = etCharacterHp.text.toString()
        val charInit = etCharacterInitModifier.text.toString()
        if (charName != "" && charAc != "" && charHp != "" && charInit != ""){
            val newCharacter = Character(
                name = charName,
                armorClass = Integer.parseInt(charAc),
                hitPoints = Integer.parseInt(charHp),
                initiativeModifier = Integer.parseInt(charInit))
            mEditCharVM.updateCharacter(mCharacter, newCharacter)
            sendResultActivity()
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * ActivityForResult's answer
     */
    private fun sendResultActivity() {
        Toast.makeText(this, getString(R.string.add_campaign_success), Toast.LENGTH_SHORT).show()
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun prepareView() {
        etCharacterName.setText(mCharacter.name)
        etCharacterAc.setText(mCharacter.armorClass.toString())
        etCharacterHp.setText(mCharacter.hitPoints.toString())
        etCharacterInitModifier.setText(mCharacter.initiativeModifier.toString())
    }
}
