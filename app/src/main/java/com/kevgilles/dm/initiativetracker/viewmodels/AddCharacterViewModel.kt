package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Team
import java.util.concurrent.Executors


class AddCharacterViewModel : ViewModel() {

    private val characterDao = App.database.characterDao()
    var mTeamId: Long = -1

    fun setTeamId(id: Long) {
        mTeamId = id
    }

    /**
     *  Add character to database
     */
    fun addCharacter(character: Character) {
        character.teamId = mTeamId
        Executors.newSingleThreadExecutor().execute {
            characterDao.insertCharacter(character)
        }
    }
}