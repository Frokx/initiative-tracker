package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Character
import java.util.concurrent.Executors

class EditCharacterViewModel: ViewModel() {
    private val characterDao = App.database.characterDao()


    fun updateCharacter(savedCharacter: Character, newCharacter: Character){
        if (savedCharacter != newCharacter) {
            newCharacter.id = savedCharacter.id
            newCharacter.teamId = savedCharacter.teamId
            Executors.newSingleThreadExecutor().execute {
                characterDao.updateCharacter(newCharacter)
            }
        }
    }
}