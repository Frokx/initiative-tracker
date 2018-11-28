package com.kevgilles.dm.initiativetracker.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevgilles.dm.initiativetracker.App
import com.kevgilles.dm.initiativetracker.dataclass.Character
import com.kevgilles.dm.initiativetracker.dataclass.Creature
import com.kevgilles.dm.initiativetracker.dataclass.Fighter
import java.security.SecureRandom
import java.util.concurrent.Executors

class TrackingViewModel : ViewModel() {
    // Necessary Daos
    private val creatureDao = App.database.creatureDao()
    private val characterDao = App.database.characterDao()

    // Every needed Ids
    private var mEncounterId: Long = -1
    private var mTeamId: Long = -1

    // Needed Lists
    private var mTrackingList: MutableList<Fighter> = ArrayList()
    private var entryListDisplayed: MutableLiveData<MutableList<Fighter>> = MutableLiveData()
    private var charactersRoll: MutableList<Character> = ArrayList()
    private var readyFighters: MutableList<Fighter> = ArrayList()

    // Flags
    private var needCharactersRolls: MutableLiveData<Boolean> = MutableLiveData()
    private var fightReady: MutableLiveData<Boolean> = MutableLiveData()

    fun needCharactersRolls() = needCharactersRolls
    fun isFightReady() = fightReady
    fun getFightersList() = readyFighters
    fun getCharactersRoll() = charactersRoll

    fun getEntries() = entryListDisplayed

    /**
     * Roll for Creatures and request roll for Characters
     */
    fun rollInitiative() {
        val random = SecureRandom()
        val executor = Executors.newSingleThreadExecutor()
        charactersRoll.clear()
        readyFighters.clear()
        executor.submit {
            mTrackingList.forEach {
                when(it) {
                    is Creature -> {
                        it.rolledInitiative = random.nextInt(20) + it.initiativeModifier
                        readyFighters.add(it)
                    }
                    is Character -> charactersRoll.add(it)
                }
            }
            Log.e("T111", readyFighters.toString())
        }
        executor.submit { needCharactersRolls.postValue(true) }
        executor.shutdown()
    }

    /**
     * Build the fighters from the encounter's creatures and the team's characters
     */
    fun prepareFight(teamId: Long, encounterId: Long) {
        if(mTrackingList.isNotEmpty()) return
        this.mTeamId = teamId
        this.mEncounterId = encounterId
        var characters: List<Character> = emptyList()
        var creatures: List<Creature> = emptyList()

        val executor = Executors.newSingleThreadExecutor()
        executor.submit { characters = characterDao.getAllCharacterFromTeam(mTeamId) }
        executor.submit { creatures = creatureDao.getAllCreaturesFromEncounterId(mEncounterId) }
        executor.submit {
            characters.forEach { mTrackingList.add(it)}
        }
        executor.submit {
            creatures.forEach { mTrackingList.add(it) }
        }
        executor.submit {
            if (mTrackingList.isNotEmpty()) {
                entryListDisplayed.postValue(mTrackingList)
            }
        }
        executor.shutdown()
    }

    fun inputCharRolls(characters: ArrayList<Character>) {
        readyFighters.addAll(characters)
        fightReady.postValue(true)
        Log.e("T112", readyFighters.toString())
    }

//    fun cancelFight() {
//        roundCount = 1
//        charactersRoll.clear()
//        needCharactersRolls.postValue(false)
//    }
//
//    fun killed(id: Int) {
//        mTrackingList.removeAt(id)
//        informView()
//    }

}