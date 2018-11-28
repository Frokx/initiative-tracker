package com.kevgilles.dm.initiativetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import java.util.concurrent.Executors

class MainActivityViewModel : ViewModel() {
    private val campaignDao = App.database.campaignDao()
    private var campaignsList: MutableLiveData<List<Campaign>> = MutableLiveData()

    fun refreshCampaignList() {
        Executors.newSingleThreadExecutor().execute {
            campaignDao.getAllCampaigns().let {
                campaignsList.postValue(it)
            }
        }
    }

    fun getCampaignsList(): MutableLiveData<List<Campaign>> = campaignsList


}