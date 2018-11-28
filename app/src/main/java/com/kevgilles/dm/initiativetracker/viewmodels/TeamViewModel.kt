package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Team
import java.util.concurrent.Executors

class TeamViewModel: ViewModel() {
    // DAOS
    private val characterDao = App.database.characterDao()
    private val teamDao = App.database.teamDao()

    // FLAGS
    private val teamNameNeeded: MutableLiveData<Boolean> = MutableLiveData()

    // DATA
    private var characterList :MutableLiveData<MutableList<Character>> = MutableLiveData()
    private var mTeamId: Long = -1
    private var mCampaignId: Long = -1

    fun getTeamId() = mTeamId
    fun needTeamName() = teamNameNeeded

    fun setIds(campaignId: Long, teamId: Long) {
        mTeamId = teamId
        mCampaignId = campaignId
        if( mTeamId == -1L) {
            teamNameNeeded.postValue(true)
        }
    }

    fun setTeamName(teamName: String) {
        if (mTeamId == -1L && mCampaignId != -1L){
            Executors.newSingleThreadExecutor().execute {
                mTeamId = teamDao.insertTeam(Team(name = teamName, campaignId = mCampaignId))
            }
        }
    }

    fun renameTeam(newName: String) {
        if(newName != "") {
            Executors.newSingleThreadExecutor().execute {
                teamDao.updateTeamName(Team(id = mTeamId, name = newName, campaignId = mCampaignId))
            }
        }
    }

    fun refreshCharacterList() {
        Executors.newSingleThreadExecutor().execute {
            characterDao.getAllCharacterFromTeam(mTeamId).let {
                characterList.postValue(it)
            }
        }
    }

    fun deleteCharacter(charId: Long) {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit { characterDao.deleteCharacter(charId) }
        executor.submit { refreshCharacterList() }
        executor.shutdown()
    }

    fun getCharacterList(): MutableLiveData<MutableList<Character>> = characterList
}