package com.kevgilles.dm.initiativetracker.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.activities.TrackingActivity
import com.kevgilles.dm.initiativetracker.dataclass.Character

class CharactersRollsFragment : DialogFragment() {
    private lateinit var mActivity: TrackingActivity
    private val etList: MutableList<EditText> = ArrayList()
    private var characterList: MutableList<Character> = ArrayList()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mActivity = activity as TrackingActivity
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.fragment_char_rolls, null)
        characterList = arguments?.getParcelableArrayList("characters")!!

        val mainLinearLayout = view?.findViewById<LinearLayout>(R.id.parent_ll)
        characterList.forEach {
            val inLinearLayout = inflater?.inflate(R.layout.dialog_char_roll, null)!!
            val name = inLinearLayout.findViewById<TextInputLayout>(R.id.tiInitScore)
            val finalScore = inLinearLayout.findViewById<EditText>(R.id.etCharacterRolledInit)!!
            val initModifier = inLinearLayout.findViewById<TextView>(R.id.tvModifier)!!
            name?.hint = it.name
            initModifier.text = "Modif: ${it.initiativeModifier}"
            etList.add(finalScore)
            mainLinearLayout?.addView(inLinearLayout)
        }

        builder.setView(view)
            .setPositiveButton("Done") { _, _ ->
                mActivity.sendCharacterRollToVM(ArrayList(prepReturnList()))
            }
            .setTitle("Roll Initiative !")
        return builder.create()
    }

    private fun prepReturnList(): MutableList<Character> {
        for (i in 0 until etList.size step 1) {
            characterList[i].rolledInitiative = Integer.parseInt(etList[i].text.toString()) + characterList[i].initiativeModifier
        }
        return characterList
    }


}