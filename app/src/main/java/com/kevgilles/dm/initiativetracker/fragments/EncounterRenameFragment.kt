package com.kevgilles.dm.initiativetracker.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.kevgilles.dm.initiativetracker.R
import com.kevgilles.dm.initiativetracker.activities.EncounterActivity
import com.kevgilles.dm.initiativetracker.activities.TeamActivity

class EncounterRenameFragment : DialogFragment() {

    // TODO: Set Current Text
    private lateinit var currentName: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mActivity = activity as EncounterActivity
        val builder = AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_encounter_name, null)
        val editText = view?.findViewById<EditText>(R.id.etEncounterName)!!
        builder.setView(view)
            .setTitle("Choose a New Encounter Name")
            .setPositiveButton("Confirm") { _, _ ->
                mActivity.updateEncounterName(editText.text.toString())
            }
            .setNegativeButton("Cancel") { _,_ ->
                mActivity.updateEncounterName(null)
            }
            .setOnDismissListener { mActivity.updateEncounterName(null) }
        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setCurrentName(name: String) { currentName = name }
}