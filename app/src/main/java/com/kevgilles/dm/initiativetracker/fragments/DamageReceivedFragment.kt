package com.kevgilles.dm.initiativetracker.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.activities.FightActivity
import com.kevgilles.dm.initiativetracker.dataclass.Fighter


class DamageReceivedFragment : DialogFragment() {
    private lateinit var mActivity: FightActivity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mActivity = activity as FightActivity
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_fighter_damage, null)!!
        val fighter = arguments?.getParcelable<Fighter>("fighter")!!
        view.findViewById<TextView>(R.id.tvMaxHp).text = "/ " + fighter.hitPoints.toString() + " HP"
        val etCurrentHp = view.findViewById<EditText>(R.id.etCurrentHp)
        etCurrentHp.setText(fighter.currentHp.toString())

        builder.setView(view)
            .setPositiveButton("Confirm") { _, _ ->
                mActivity.updateFighterHealth(Integer.parseInt(etCurrentHp.text.toString()))
            }
            .setTitle("New HP Value")
            .setNegativeButton("Cancel") { _, _ ->
                mActivity.cancelFighterHealth()
            }
        return builder.create()
    }


}