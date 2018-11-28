package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import com.kevgilles.dm.initiativetracker.dataclass.EncounterCreature
import java.util.concurrent.Executors

class AddCreatureViewModel : ViewModel() {

    private val creatureDao = App.database.creatureDao()
    private val encounterCreatureDao = App.database.encounterCreatureDao()
    private var creaturesForEncounter: Long = -1

    /**
     *  Add creature to database
     */
    fun insertCreature(creature: Creature, encounterId: Long) {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit { creaturesForEncounter = creatureDao.insertCreature(creature) }
        executor.submit { insertEncounterCreature(encounterId) }
        executor.submit { creaturesForEncounter = -1 }
        executor.shutdown()
    }

    /**
     * Create Bridge between Encounters & Creatures
     */
    private fun insertEncounterCreature(encounterId: Long) {
        if (creaturesForEncounter != -1L) {
                encounterCreatureDao.insertEncounterCreature(
                    EncounterCreature(encounterId = encounterId, creatureId = creaturesForEncounter)
                )
            }
        }
    }
