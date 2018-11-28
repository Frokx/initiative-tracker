package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import java.util.concurrent.Executors

class EditCreatureViewModel: ViewModel() {
    private val creatureDao = App.database.creatureDao()


    fun updateCreature(savedCreature: Creature, newCreature: Creature){
        if (savedCreature != newCreature) {
            newCreature.id = savedCreature.id
            Executors.newSingleThreadExecutor().execute {
                creatureDao.updateCreature(newCreature)
            }
        }
    }
}