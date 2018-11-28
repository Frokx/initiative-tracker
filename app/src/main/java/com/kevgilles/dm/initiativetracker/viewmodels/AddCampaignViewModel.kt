package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import java.util.concurrent.Executors

sealed class AddCampaignState(
    val campaignId: Long = -1
)

class AddCampaignStateSuccess(id: Long) : AddCampaignState(campaignId = id)
object AddCampaignStateFailure : AddCampaignState()

class AddCampaignViewModel : ViewModel() {
    private val campaignDao = App.database.campaignDao()
    private var state = MutableLiveData<AddCampaignState>()

    /**
     *  Add Campaign to database
     */
    fun addCampaign(name: String, game: String) {
        Executors.newSingleThreadExecutor().execute {
            val id = campaignDao.insertCampaign(Campaign(name = name, game = game))
        if (id != -1L)  state.postValue(AddCampaignStateSuccess(id))
        else state.postValue(AddCampaignStateFailure)
        }
    }

    /**
     * Observable function
     */
    fun getState(): MutableLiveData<AddCampaignState> = state
}