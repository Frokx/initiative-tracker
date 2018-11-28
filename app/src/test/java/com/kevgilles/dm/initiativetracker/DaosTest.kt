package com.kevgilles.dm.initiativetracker

import android.content.Context
import androidx.room.Room
import com.kevgilles.dm.initiativetracker.database.AppDatabase
import com.kevgilles.dm.initiativetracker.database.CampaignDao
import com.kevgilles.dm.initiativetracker.dataclass.Campaign
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.mock

open class DaosTest {
    var context = mock(Context::class.java)!!
    private lateinit var database: AppDatabase
    private lateinit var campaignDao: CampaignDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        campaignDao = database.campaignDao()
    }

    @Test
    fun insertCampaign() {
        val campaignA = Campaign(name = "Campaign A")
        campaignA.id = campaignDao.insertCampaign(campaignA)
        assertEquals(4, 2 + 2)
        assertNotEquals(0L, campaignA.id)
    }
}
