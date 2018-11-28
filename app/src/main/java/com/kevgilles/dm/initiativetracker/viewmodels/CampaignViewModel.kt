package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Encounter
import com.kevgilles.dm.initiativetracker.dataclass.Team
import java.util.concurrent.Executors

class CampaignViewModel : ViewModel() {
    private var encounterList: MutableLiveData<MutableList<Encounter>> = MutableLiveData()
    private val teamDao = App.database.teamDao()
    private val encounterDao = App.database.encounterDao()
    private var team: MutableLiveData<Team> = MutableLiveData()

    fun refreshTeam(id: Long) {
        Executors.newSingleThreadExecutor().execute {
            team.postValue(teamDao.getTeamFromCampaignId(id))
        }
    }

    fun refreshEncounters(id: Long) {
        Executors.newSingleThreadExecutor().execute {
            encounterList.postValue(encounterDao.getAllEncountersFromCampaign(id))
        }
    }

    fun deleteEncounter(encounterId: Long, campaignId: Long) {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit { encounterDao.deleteEncounter(encounterId) }
        executor.submit { refreshEncounters(campaignId) }
        executor.shutdown()
    }

    fun getEncounters() = encounterList

    fun getTeam(): MutableLiveData<Team> = team
}