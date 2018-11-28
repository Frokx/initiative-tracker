package com.kevgilles.dm.initiativetracker.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.activities.TeamActivity

class TeamNameFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mActivity = activity as TeamActivity
        val builder = AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_team_name, null)
        val editText = view?.findViewById<EditText>(R.id.etTeamName)!!
        builder.setView(view)
            .setTitle("Choose a Team Name")
            .setPositiveButton("Confirm") { _, _ ->
                mActivity.setTeamName(editText.text.toString())
            }
            .setNegativeButton("Cancel") { _,_ ->
                mActivity.setTeamName(null)
            }
            .setOnDismissListener { mActivity.setTeamName(null) }
        return builder.create()
    }
}