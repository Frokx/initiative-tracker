package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import com.kevgilles.dm.initiativetracker.dataclass.Encounter
import java.util.concurrent.Executors

class EncounterViewModel: ViewModel() {
    private val creatureDao = App.database.creatureDao()
    private val encounterDao = App.database.encounterDao()

    // FLAGS
    private val encounterNameNeeded: MutableLiveData<Boolean> = MutableLiveData()

    // DATA
    private var creatureList :MutableLiveData<MutableList<Creature>> = MutableLiveData()
    private var mEncounterId: Long = -1
    private var mCampaignId: Long = -1

    fun getCreatureList() = creatureList
    fun getEncounterId() = mEncounterId
    fun needEncounterName() = encounterNameNeeded

    fun refreshCreatures() {
        Executors.newSingleThreadExecutor().execute {
            creatureList.postValue(creatureDao.getAllCreaturesFromEncounterId(mEncounterId))
        }
    }

    fun setIds(encounterId: Long, campaignId: Long) {
        mEncounterId = encounterId
        mCampaignId = campaignId
        if( mEncounterId == -1L) {
            encounterNameNeeded.postValue(true)
        }
    }

    fun renameEncounter(newName: String) {
        if (newName != ""){
            Executors.newSingleThreadExecutor().execute {
                encounterDao.updateEncounter(Encounter(id = mEncounterId, name = newName, campaignId = mCampaignId))
            }
        }
    }

    fun setEncounterName(teamName: String) {
        if (mEncounterId == -1L && mCampaignId != -1L){
            Executors.newSingleThreadExecutor().execute {
                mEncounterId = encounterDao.insertEncounter(Encounter(name = teamName, campaignId = mCampaignId))
            }
        }
    }

    fun deleteCreature(creatureId: Long) {
        Executors.newSingleThreadExecutor().execute {
             creatureDao.deleteCreature(creatureId)
        }
        refreshCreatures()
    }
}